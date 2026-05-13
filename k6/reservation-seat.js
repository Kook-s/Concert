import http from 'k6/http';
import { Counter } from 'k6/metrics';

const reservationSuccess = new Counter('reservation_success');
const reservationLockFail = new Counter('reservation_lock_fail');
const reservationDbError = new Counter('reservation_db_error');
const reservationOtherFail = new Counter('reservation_other_fail');

const USER_IDS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

export const options = {
    scenarios: {
        reserve_same_seat_once: {
            executor: 'per-vu-iterations',
            vus: USER_IDS.length,
            iterations: 1,
            maxDuration: '30s',
        },
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export function setup() {
    const tokens = [];

    for (const userId of USER_IDS) {
        const issuePayload = JSON.stringify({ userId });

        const issueRes = http.post(
            `${BASE_URL}/api/v1/queue/tokens`,
            issuePayload,
            {
                headers: { 'Content-Type': 'application/json' },
            }
        );

        console.log(`issueToken userId=${userId}, status=${issueRes.status}, body=${issueRes.body}`);

        const body = JSON.parse(issueRes.body);

        if (body.errorCode) {
            throw new Error(`토큰 발급 실패 userId=${userId}, body=${issueRes.body}`);
        }

        tokens.push({
            userId,
            token: body.token,
        });
    }

    return { tokens };
}

export default function (data) {
    const current = data.tokens[__VU - 1];

    const payload = JSON.stringify({
        userId: current.userId,
        concertId: 1,
        scheduleId: 1,
        seatId: 3,
    });

    const res = http.post(
        `${BASE_URL}/api/v1/reservations`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                'Token': current.token,
            },
        }
    );

    console.log(`VU=${__VU}, userId=${current.userId}, status=${res.status}, body=${res.body}`);

    const body = JSON.parse(res.body);

    if (!body.errorCode) {
        reservationSuccess.add(1);
    } else if (String(body.payload || '').includes('락 획득 실패')) {
        reservationLockFail.add(1);
    } else if (String(body.payload || '').includes('TransientPropertyValueException')) {
        reservationDbError.add(1);
    } else {
        reservationOtherFail.add(1);
    }
}
