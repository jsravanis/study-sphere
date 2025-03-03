import React, { useEffect, useState } from 'react';

const FlashCards = ({ studyGroupId }) => {
    const [flashCards, setFlashCards] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [newCard, setNewCard] = useState({ question: '', answer: '' });
    const [flipped, setFlipped] = useState(false); // State to toggle flip
    const [practiceComplete, setPracticeComplete] = useState(false); // Track if practice is complete

    // Fetch flashcards from API
    useEffect(() => {
        const fetchFlashCards = async () => {
            try {
                const response = await fetch(`http://localhost:8080/flashcards/${studyGroupId}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });
                const data = await response.json();
                setFlashCards(data);
            } catch (error) {
                console.error("Error fetching flashcards:", error);
            }
        };
        fetchFlashCards();
    }, [studyGroupId]);

    // Add new flashcard
    const handleAddFlashCard = async () => {
        if (newCard.question && newCard.answer) {
            try {
                const response = await fetch('http://localhost:8080/flashcards/create', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                    body: JSON.stringify({
                        studyGroupId,
                        userId: 1, // Replace with actual user ID from context or state
                        question: newCard.question,
                        answer: newCard.answer,
                    }),
                });
                const data = await response.json();
                setFlashCards([...flashCards, data]); // Add new flashcard to the list
                setNewCard({ question: '', answer: '' }); // Reset form
            } catch (error) {
                console.error("Error adding flashcard:", error);
            }
        } else {
            alert('Please fill in both question and answer.');
        }
    };

    // Handle navigation between flashcards
    const nextCard = () => {
        if (currentIndex < flashCards.length - 1) {
            setCurrentIndex((prevIndex) => prevIndex + 1);
            setFlipped(false); // Reset flip on new card
        } else {
            setPracticeComplete(true); // Set practice complete when last card is reached
        }
    };

    const prevCard = () => {
        if (currentIndex !== 0) {
            setCurrentIndex((prevIndex) => (prevIndex - 1 + flashCards.length) % flashCards.length);
            setFlipped(false); // Reset flip on new card
        } else {
            setCurrentIndex(currentIndex); // Set practice complete when last card is reached
        }
    };

    // Handle card flip
    const toggleFlip = () => {
        setFlipped(!flipped);
    };

    function restartPractice() {
        setPracticeComplete(false);
        setCurrentIndex(0);
        setFlipped(false);
    }

    return (
        <div>
            <h2 className="text-xl font-bold mb-4">Flash Cards</h2>

            {/* Flashcard display */}
            {flashCards.length > 0 && !practiceComplete ? (
                <div className="flashcard-main-container">
                    <div
                        className="flashcard-container p-4 border rounded shadow-lg"
                        onClick={toggleFlip} // Flip on click
                    >
                        <div className={`flashcard p-4 cursor-pointer ${flipped ? 'flip' : ''}`}>
                            <div className="flashcard-content">
                                {flipped ? (
                                    <div className="flashcard-answer mb-2">
                                        <strong>A:</strong> {flashCards[currentIndex].answer}
                                    </div>
                                ) : (
                                    <div className="flashcard-question mb-2">
                                        <strong>Q:</strong> {flashCards[currentIndex].question}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                    <div className="flex justify-between mt-4">
                        <button onClick={prevCard} className="flashcard-button bg-peach p-2 rounded w-50">
                            Previous
                        </button>
                        <button onClick={nextCard} className="flashcard-button bg-peach p-2 rounded w-50">
                            Next
                        </button>
                    </div>
                </div>
            ) : (
                practiceComplete && (
                    <div className="flashcard-main-container">
                        <div
                            className="flashcard-container p-4 border rounded shadow-lg text-center font-semibold text-xl">
                            <div className="flashcard">
                                <h2 className="text-xl text-gray-800">Practice Complete! Great job!</h2>
                            </div>
                        </div>
                        <div className="justify-center flex mt-4">
                            <button onClick={restartPractice} className="flashcard-button bg-gray-200 p-2 rounded w-50">
                                Restart
                            </button>
                        </div>
                    </div>
                )
            )}

            {/* Add new flashcard form */}
            <div className="m-auto" style={{ width: '800px'}}>
                <h3 className="text-lg font-semibold mt-8">Add a New Flashcard</h3>
                <div className="flex flex-col space-y-4">
                    <input
                        type="text"
                        className="border p-2 rounded mt-2"
                        placeholder="Question"
                        value={newCard.question}
                        onChange={(e) => setNewCard({ ...newCard, question: e.target.value })}
                    />
                    <input
                        type="text"
                        className="border p-2 rounded"
                        placeholder="Answer"
                        value={newCard.answer}
                        onChange={(e) => setNewCard({ ...newCard, answer: e.target.value })}
                    />
                    <button
                        onClick={handleAddFlashCard}
                        className="bg-peach text-white p-2 rounded"
                    >
                        Add Flashcard
                    </button>
                </div>
            </div>
        </div>
    );
};

export default FlashCards;
