import React, {useEffect, useState} from 'react'
import NavBar from "./NavBar.jsx";

const PublicGroups = () => {
    const [publicGroups, setPublicGroups] = useState([]);
    let userId = localStorage.getItem("userId");

    useEffect(() => {
        const fetchPublicGroups = async () => {
            try {
                const response = await fetch(`http://localhost:8080/studygroups/public/${userId}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });
                const data = await response.json();
                setPublicGroups(data);
            } catch (error) {
                console.error("Error fetching public groups:", error);
            }
        };
        fetchPublicGroups();
    }, []);

    const handleJoinGroup = async (studyGroupId) => {
        try {
            const response = await fetch(`http://localhost:8080/studygroups/${studyGroupId}/addUserToGroup/${userId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            if (response.ok) {
                const updatedPublicGroups = publicGroups.filter((group) => group.studyGroupId !== studyGroupId);
                setPublicGroups(updatedPublicGroups);
            }
        } catch (error) {
            console.error("Error joining group:", error);
        }
    };

    return (
        <div className="min-h-screen bg-white">
            <NavBar />
            <div className="max-w-5xl mx-auto mt-6 p-4">
                <h2 className="text-xl font-semibold mb-4">Public Groups</h2>
                {publicGroups.length > 0 ? (
                    <div className="space-y-2">
                        {publicGroups.map((group) => (
                            <div key={group.studyGroupId}
                                 className="flex items-center bg-white p-3 rounded-lg border border-light-peach shadow justify-between">
                                <div className="flex flex-col gap-y-0.5">
                                    <h2 className="font-semibold">{group.topic}</h2>
                                    <p className="text-gray-600">{group.description}</p>
                                    <p className="text-gray-600">
                                    Members: {group.currentUserCount} / {group.maxLimit}
                                </p>
                                </div>
                                <button className="h-fit w-25" onClick={() => handleJoinGroup(group.studyGroupId)}>
                                    Join
                                </button>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>No public groups available.</p>
                )}
            </div>
        </div>
    );
};
export default PublicGroups;
