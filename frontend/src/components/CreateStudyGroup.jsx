import React, { useState } from 'react';
import NavBar from "./NavBar.jsx";

const CreateStudyGroup = () => {
    const [isPrivate, setIsPrivate] = useState(false);
    const [emails, setEmails] = useState([]);
    const [emailInput, setEmailInput] = useState("");
    const [topic, setTopic] = useState("");
    const [description, setDescription] = useState("");
    const [maxMembers, setMaxMembers] = useState("");
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const handleIsPrivateCheckbox = (event) => {
        setIsPrivate(event.target.checked);
    };

    const handleEmailKeyDown = (event) => {
        if (event.key === "Enter" && emailInput.trim() !== "") {
            event.preventDefault();
            if (!emails.includes(emailInput.trim())) {
                setEmails([...emails, emailInput.trim()]);
                setEmailInput("");
            }
        }
    };

    const removeEmail = (emailToRemove) => {
        setEmails(emails.filter(email => email !== emailToRemove));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setMessage("");

        const studyGroupData = {
            topic,
            description,
            maxLimit: parseInt(maxMembers, 10),
            createdUserId: parseInt(localStorage.getItem("userId")),
            isPrivate,
            invitedUserEmails: isPrivate ? emails : [],
        };

        try {
            const response = await fetch("http://localhost:8080/studygroups/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
                body: JSON.stringify(studyGroupData),
            });

            if (!response.ok) {
                throw new Error("Failed to create study group");
            }
            await response.json();
            setMessage(`Study group created successfully!`);
        } catch (error) {
            setMessage("Error creating study group. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-white">
            <NavBar />
            <div className="max-w-lg mx-auto mt-10 p-6 bg-white shadow-lg rounded-xl">
                <h1 className="text-2xl font-semibold mb-4">Create Study Group</h1>
                {message && <p className="text-center text-red-500 mb-3">{message}</p>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block font-medium mb-1">Topic:</label>
                        <input
                            type="text"
                            required
                            value={topic}
                            onChange={(e) => setTopic(e.target.value)}
                            className="form-input text-gray-600"
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block font-medium mb-1">Description:</label>
                        <textarea
                            required
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            className="form-input text-gray-600"
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block font-medium mb-1">Max Members:</label>
                        <input
                            type="number"
                            required
                            value={maxMembers}
                            onChange={(e) => setMaxMembers(e.target.value)}
                            className="form-input text-gray-600"
                        />
                    </div>
                    <div className="mb-4 flex items-center">
                        <label className="font-medium mr-2">Private:</label>
                        <input type="checkbox" checked={isPrivate} onChange={handleIsPrivateCheckbox} />
                    </div>

                    {isPrivate && (
                        <div className="mb-4">
                            <label className="block font-medium mb-1">Invite Members:</label>
                            <div className="flex flex-wrap border p-2 rounded-md min-h-[40px]">
                                {emails.map((email, index) => (
                                    <span key={index} className="bg-lighter-peach text-gray-600 px-2 py-1 rounded-md m-0.5 mr-1 flex items-center">
                                        {email}
                                        <button
                                            type="button"
                                            className="ml-2 text-peach"
                                            onClick={() => removeEmail(email)}
                                        >
                                            ×
                                        </button>
                                    </span>
                                ))}
                                <input
                                    type="text"
                                    placeholder="Enter email and press Enter"
                                    value={emailInput}
                                    onChange={(e) => setEmailInput(e.target.value)}
                                    onKeyDown={handleEmailKeyDown}
                                    className="form-input text-gray-600"
                                />
                            </div>
                        </div>
                    )}

                    <button
                        type="submit"
                        className="bg-peach text-white px-4 py-2 rounded-md w-full mt-3"
                        disabled={loading}
                    >
                        {loading ? "Creating..." : "Create Group"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CreateStudyGroup;
