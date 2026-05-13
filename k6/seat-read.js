import http from 'k6/http';
import { Counter, Rate } from 'k6/metrics';

const readSuccess = new Counter('seat_read_success');
const readFailure = new Counter('seat_read_failure');
const readErrorRate = new Rate('seat_read_error_rate');

const BASE_URL = __ENV.BASE_URL || 'http://127.0.0.1:8080';

export const options = {
  scenarios: {
    read_available_seats: {
      executor: 'constant-vus',
      vus: 30,
      duration: '20s',
    },
  },
};

export function setup() {
  const res = http.post(
    `${BASE_URL}/api/v1/queue/tokens`,
    JSON.stringify({ userId: 1 }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  const body = JSON.parse(res.body);
  return { token: body.token };
}

export default function (data) {
  const res = http.get(
    `${BASE_URL}/api/v1/concerts/1/schedules/1/seats`,
    { headers: { Token: data.token } }
  );

  const success = res.status === 200;
  readErrorRate.add(!success);

  if (success) {
    readSuccess.add(1);
  } else {
    readFailure.add(1);
  }
}
