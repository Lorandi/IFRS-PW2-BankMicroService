import * as jose from 'jose';
import fs from 'fs';

// Lê a chave privada PEM
const privateKeyPem = fs.readFileSync('./privateKey.pem', 'utf8');
const privateKey = await jose.importPKCS8(privateKeyPem, 'RS256');

// Payload
const payload = {
    iss: 'ifrs-bank',
    upn: 'admin',
    groups: ['ADMIN'],
    userId: 1,
    iat: Math.floor(Date.now() / 1000),
    exp: Math.floor(Date.now() / 1000) + 600, // expira em 10 minutos
};

// Gera o token
const jwt = await new jose.SignJWT(payload)
    .setProtectedHeader({ alg: 'RS256', typ: 'JWT' })
    .sign(privateKey);

console.log(jwt);