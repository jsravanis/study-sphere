import React, { useEffect, useState } from 'react';
import WebSocketService from '../services/WebSocketService';

const ChatComponent = ({ studyGroupId, userId }) => {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');

    useEffect(() => {
        const fetchChatHistory = async () => {
            const response = await fetch(`http://localhost:8080/chat/history/${studyGroupId}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            const data = await response.json();
            setMessages(data);
        };
        fetchChatHistory();

        WebSocketService.connect(studyGroupId, (message) => {
            setMessages((prevMessages) => [...prevMessages, message]);
        });

        return () => {
            WebSocketService.disconnect();
        };
    }, [studyGroupId]);

    const handleSendMessage = () => {
        if (newMessage.trim()) {
            WebSocketService.sendMessage(studyGroupId, userId, newMessage);
            setNewMessage('');
        }
    };

    return (
        <div className="p-4 border rounded-lg bg-white flex flex-col h-full">
            <div className="flex flex-col overscroll-y-auto mb-4 mt-auto">
                {messages.map((msg) => (
                    <div key={msg.messageId} className="flex flex-col mx-20">
                        <div className={`mb-2 p-2 rounded-3xl w-100
                         ${Number(msg.sender.userId) === Number(userId)
                                 ? 'self-end bg-lighter-peach'
                                 : 'self-start bg-gray-50'}`}>
                            <p className="text-xs text-gray-600">{msg.sender.username}</p>
                            <p>{msg.message}</p>
                        </div>
                    </div>
                ))}
            </div>
            <div className="flex">
                <input
                    type="text"
                    className="flex-1 border p-2 rounded"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Type a message..."
                />
                <button className="ml-2 bg-peach text-white p-2 rounded" onClick={handleSendMessage}>Send</button>
            </div>
        </div>
    );
};

export default ChatComponent;