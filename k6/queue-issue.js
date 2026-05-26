import http from 'k6/http';
import { Counter, Rate } from 'k6/metrics';

const queueSuccess = new Counter('queue_success');
const queueFailure = new Counter('queue_failure');
const queueErrorRate = new Rate('queue_error_rate');

const BASE_URL = __ENV.BASE_URL || 'http://127.0.0.1:8080';
const USER_IDS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

export const options = {
  scenarios: {
    issue_tokens: {
      executor: 'shared-iterations',
      vus: 50,
      iterations: 300,
      maxDuration: '30s',
    },
  },
};

export default function () {
  const userId = USER_IDS[(__ITER + __VU) % USER_IDS.length];
  const res = http.post(
    `${BASE_URL}/api/v1/queue/tokens`,
    JSON.stringify({ userId }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  const success = res.status === 201;
  queueErrorRate.add(!success);

  if (success) {
    queueSuccess.add(1);
  } else {
    queueFailure.add(1);
  }
}
