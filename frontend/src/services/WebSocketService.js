import { Client } from '@stomp/stompjs';

let client;
let onMessageCallback;

const connect = (groupId, onMessage) => {
    client = new Client({
        brokerURL: 'ws://localhost:8080/study-group-ws',
        onConnect: () => {
            console.log('Connected to WebSocket');
            client.subscribe(`/topic/chat/${groupId}`, (message) => {
                if (onMessage) onMessage(JSON.parse(message.body));
            });
        },
    });
    onMessageCallback = onMessage;
    client.activate();
};

const sendMessage = (groupId, senderId, message) => {
    if (client && client.connected) {
        const chatMessage = {
            groupId,
            senderId,
            message,
        };
        client.publish({
            destination: `/app/chat/${groupId}`,
            body: JSON.stringify(chatMessage),
            headers: {
                'content-type': 'application/json'
            } });
    }
};

const disconnect = () => {
    if (client) {
        client.deactivate();
        console.log('Disconnected from WebSocket');
    }
};

export default { connect, sendMessage, disconnect };