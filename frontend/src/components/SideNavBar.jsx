import React from 'react';

const SideNavBar = ({ activeTab, setActiveTab }) => {
    return (
        <div className="w-64 bg-white border-r p-4">
            <nav>
                <ul className="space-y-2">
                    <li
                        className={`cursor-pointer p-2 rounded ${activeTab === 'info' ? 'bg-peach text-white' : ''}`}
                        onClick={() => setActiveTab('info')}>
                        Group Info
                    </li>
                    <li
                        className={`cursor-pointer p-2 rounded ${activeTab === 'flashcards' ? 'bg-peach text-white' : ''}`}
                        onClick={() => setActiveTab('flashcards')}>
                        Flash Cards
                    </li>
                    <li
                        className={`cursor-pointer p-2 rounded ${activeTab === 'chat' ? 'bg-peach text-white' : ''}`}
                        onClick={() => setActiveTab('chat')}>
                        Chat
                    </li>
                    <li
                        className={`cursor-pointer p-2 rounded ${activeTab === 'notes' ? 'bg-peach text-white' : ''}`}
                        onClick={() => setActiveTab('notes')}>
                        Notes
                    </li>
                </ul>
            </nav>
        </div>
    );
};

export default SideNavBar;
