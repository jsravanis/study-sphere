import React, { useEffect, useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import NavBar from "./NavBar"; // Import the NavBar

const Dashboard = () => {
    const [userData, setUserData] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            const userId = localStorage.getItem("userId");
            const response = await fetch(`http://localhost:8080/users/${userId}/profile`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            const data = await response.json();
            setUserData(data);
        };

        fetchUserData();
    }, []);

    const handleAcceptInvitation = async (invitationId) => {
        try {
            const response = await fetch(`http://localhost:8080/studygroups/acceptInvitation/${invitationId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            if (response.ok) {
                const updatedInvitations = userData.invitations.filter((invitation) => invitation.invitationId !== invitationId);
                setUserData({ ...userData, invitations: updatedInvitations });
            }
        }
        catch (error) {
            console.error("Error accepting invitation:", error);
        }
    }

    const handleDeclineInvitation = async (invitationId) => {
        try {
            const response = await fetch(`http://localhost:8080/studygroups/declineInvitation/${invitationId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            if (response.ok) {
                const updatedInvitations = userData.invitations.filter((invitation) => invitation.invitationId !== invitationId);
                setUserData({ ...userData, invitations: updatedInvitations });
            }
        }
        catch (error) {
            console.error("Error declining invitation:", error);
        }
    }

    if (!userData) {
        return <div>Loading...</div>;
    }

    return (
        <div className="min-h-screen bg-white">
            <NavBar /> {/* Include the NavBar here */}

            <div className="max-w-5xl mx-auto mt-6 p-4">
                <h2 className="text-3xl mb-4 font-semibold">Welcome, {userData.username}</h2>
                <h2 className="text-xl font-semibold mb-4">Private Group Requests</h2>
                {userData.invitations.length > 0 ? (
                    <div className="space-y-2">
                        {userData.invitations.map((invitation) => (
                            <div key={invitation.invitationId} className="flex justify-between items-center bg-white p-3 rounded-lg border border-light-peach shadow">
                                <span>{invitation.studyGroup.topic} by {invitation.createdUser.username}</span>
                                <div>
                                    <button className="bg-light-peach text-black px-3 py-1 rounded mr-2"
                                            onClick={() => handleAcceptInvitation(invitation.invitationId)}>
                                            Join
                                    </button>
                                    <button className="bg-light-peach text-black px-3 py-1 rounded"
                                            onClick={() => handleDeclineInvitation(invitation.invitationId)}>
                                            Ignore
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>No new requests.</p>
                )}

                <h2 className="text-xl font-semibold mt-6">My Groups</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-3">
                    {userData.studyGroups.map((group) => (
                        <div
                            key={group.studyGroupId}
                            className="bg-white p-4 rounded-lg shadow cursor-pointer border border-light-peach hover:shadow-lg"
                            onClick={() => navigate(`/study-group/${group.studyGroupId}`)}
                        >
                            <h3 className="font-bold">{group.topic}</h3>
                            <p className="text-gray-600">{group.description}</p>
                            <p className="text-gray-400 text-sm py-1">{group.role}</p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
