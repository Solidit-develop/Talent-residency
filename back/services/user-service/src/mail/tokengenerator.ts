import { randomBytes } from 'crypto';

function generateRandomToken(): Promise<string> {
    return new Promise((resolve, reject) => {
        randomBytes(32, (err, buffer) => {
            if (err) {
                reject(err);
            } else {
                const token = buffer.toString('hex');
                resolve(token);
            }
        });
    });
}

export default generateRandomToken;