import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import SideNavBar from '../components/SideNavBar.jsx';
import NavBar from "../components/NavBar.jsx";
import FlashCards from "./FlashCards.jsx";
import ChatComponent from "./ChatComponent.jsx";
import Notes from "./Notes.jsx";

const StudyGroup = () => {
    const { studyGroupId } = useParams();
    const [groupInfo, setGroupInfo] = useState(null);
    const [activeTab, setActiveTab] = useState('info');
    const [userEmail, setUserEmail] = useState("");
    const userId = localStorage.getItem("userId");

    useEffect(() => {
        const fetchGroupInfo = async () => {
            try {
                const response = await fetch(`http://localhost:8080/studygroups/${studyGroupId}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });
                const data = await response.json();
                setGroupInfo(data);
            } catch (error) {
                console.error("Error fetching study group info:", error);
            }
        };
        fetchGroupInfo();
    }, [studyGroupId]);

    const handleInviteUser = async (studyGroupId, userEmail, createdUserId) => {
        try {
            const response = await fetch(`http://localhost:8080/studygroups/inviteUser`, {
                method: "POST",
                body: JSON.stringify({
                    studyGroupId: parseInt(studyGroupId),
                    invitedUserEmail: userEmail,
                    createdUserId: parseInt(createdUserId)
                }),
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            if (response.ok) {
                setUserEmail("");
                alert("Invitation sent successfully!");
            }
        }
        catch (error) {
            alert("Error inviting user");
            console.error("Error inviting user:", error);
        }
    }

    if (!groupInfo) return <div>Loading...</div>;

    return (
        <div className="h-screen flex flex-col">
            <NavBar />
            <div className="flex flex-auto w-full">
                <SideNavBar activeTab={activeTab} setActiveTab={setActiveTab} />
                <div className="p-6 w-full bg-white">
                    {activeTab === 'info' && (
                        <div>
                            <div className="flex items-center gap-4">
                                <h1 className="text-2xl font-bold">{groupInfo.studyGroup.topic}</h1>
                                {/* Privacy Indicator */}
                                <p className={`px-3 py-1 text-sm font-semibold border-2 rounded-full border-peach text-peach`}>
                                    {groupInfo.studyGroup.isPrivate ? 'Private Group' : 'Public Group'}
                                </p>
                            </div>


                            <p className="mt-4 text-gray-600">{groupInfo.studyGroup.description}</p>


                            {/* Group Details */}
                            <p className="mt-2 text-gray-700">
                                Members: {groupInfo.studyGroup.currentUserCount} / {groupInfo.studyGroup.maxLimit}
                            </p>

                            {/* Users List with Roles */}
                            <div className="mt-4 max-w-sm">
                                <h2 className="text-lg font-semibold">Members</h2>
                                <ul className="mt-2 space-y-2">
                                    {groupInfo.usersWithRoles.map((user) => (
                                        <li key={user.username}
                                            className="p-2 bg-gray-100 rounded-md flex justify-between">
                                            <span className="text-gray-800">{user.username}</span>
                                            <span className="text-sm text-gray-600">{user.role}</span>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                            {/* Invite Section */}
                            {(groupInfo.studyGroup.isPrivate &&
                                groupInfo.studyGroup.currentUserCount < groupInfo.studyGroup.maxLimit) && (
                            <div className="mt-4 max-w-sm">
                                <h2 className="text-lg font-semibold">Invite Members</h2>
                                <div className="flex gap-2 mt-2">
                                    <input
                                        type="email"
                                        placeholder="Email"
                                        className="form-input"
                                        value={userEmail} // You might want to bind value to a state for userEmail
                                        onChange={(e) => setUserEmail(e.target.value)} // Update state with the email input
                                    />
                                    <button
                                        className="bg-peach text-white px-4 py-2 rounded-md"
                                        onClick={() => handleInviteUser(studyGroupId, userEmail, userId)} // Use arrow function to pass the email when clicked
                                    >
                                        Invite
                                    </button>
                                </div>
                            </div>
                            )}
                        </div>
                    )}
                    {activeTab === 'flashcards' && <FlashCards studyGroupId={studyGroupId}/>}
                    {activeTab === 'chat' && <ChatComponent studyGroupId={studyGroupId} userId={userId}/>}
                    {activeTab === 'notes' && <Notes studyGroupId={studyGroupId} userId={userId}/>}
                </div>
            </div>
        </div>
    );
};

export default StudyGroup;
