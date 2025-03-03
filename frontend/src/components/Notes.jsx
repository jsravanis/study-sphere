import React, { useState, useEffect, useRef } from "react";

const Notes = ({ studyGroupId, userId }) => {
    const [documents, setDocuments] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    // Ref to store pdf URLs for each document
    const pdfUrls = useRef({});

    useEffect(() => {
        fetchDocuments();
    }, []);

    // Fetch document list for the study group
    const fetchDocuments = async () => {
        try {
            const response = await fetch(`http://localhost:8080/pdfs/studyGroup/${studyGroupId}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });
            if (!response.ok) throw new Error("Failed to fetch documents");

            const data = await response.json();
            setDocuments(data);
            fetchPdfUrls(data); // Fetch all PDF URLs once the documents are fetched
        } catch (error) {
            console.error("Error fetching documents:", error);
        }
    };

    // Fetch PDF URLs for all documents
    const fetchPdfUrls = async (documents) => {
        for (const doc of documents) {
            if (!pdfUrls.current[doc.id]) {  // Only fetch if not already fetched
                try {
                    const response = await fetch(`http://localhost:8080/pdfs/download/${encodeURIComponent(doc.fileName)}`, {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem("token")}`,
                        },
                    });

                    if (!response.ok) throw new Error("Unauthorized");

                    const blob = await response.blob();
                    const url = URL.createObjectURL(blob);
                    pdfUrls.current[doc.id] = url; // Store the Blob URL for later use

                } catch (error) {
                    console.error(`Error fetching PDF for ${doc.fileName}:`, error);
                }
            }
        }
    };

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            setMessage("Please select a file to upload.");
            return;
        }

        setLoading(true);
        setMessage("");

        const formData = new FormData();
        formData.append("studyGroupId", studyGroupId);
        formData.append("userId", userId);
        formData.append("file", selectedFile);

        try {
            const response = await fetch("http://localhost:8080/pdfs/upload", {
                method: "POST",
                body: formData,
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            });

            if (!response.ok) throw new Error("Failed to upload document");

            setMessage("File uploaded successfully!");
            setSelectedFile(null);
            fetchDocuments(); // Refresh documents list
        } catch (error) {
            setMessage("Error uploading document. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-xl">
            <h1 className="text-2xl font-semibold mb-4">Study Group Notes</h1>

            {/* Upload Section */}
            <div className="mb-4 flex justify-between">
                <div className="flex flex-col">
                    <label className="block font-medium mb-1">Choose file to share with the group</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        onChange={handleFileChange}
                        className="mb-2"
                    />
                    {message && <p className="text-red-500">{message}</p>}
                </div>
                <button
                    onClick={handleUpload}
                    disabled={loading}
                    className="bg-peach text-white px-4 py-2 rounded-md w-50 h-fit"
                >
                    {loading ? "Uploading..." : "Upload Document"}
                </button>
            </div>

            {/* Grid Display */}
            <div className="grid grid-cols-3 gap-4">
                {documents.map((doc) => (
                    <div key={doc.id} className="border p-3 rounded-md shadow-md">
                        {/* PDF Preview with Authentication */}
                        <div className="h-32 flex items-center justify-center bg-gray-200 text-gray-500 text-sm">
                            {pdfUrls.current[doc.id] && (
                                <iframe
                                    src={pdfUrls.current[doc.id]} // Use the Blob URL for preview
                                    title={doc.fileName}
                                    className="w-full h-full"
                                    id={`pdf-preview-${doc.id}`}
                                ></iframe>
                            )}
                        </div>

                        <p className="mt-2 font-medium">{doc.fileName}</p>

                        <div className="mt-2 flex justify-between">
                            {/* View PDF - Sends Authenticated Request */}
                            <button
                                className="text-peach"
                                onClick={() => {
                                    if (pdfUrls.current[doc.id]) {
                                        window.open(pdfUrls.current[doc.id], "_blank"); // Open the same Blob URL in a new tab
                                    }
                                }}
                            >
                                View
                            </button>

                            {/* Download PDF - Sends Authenticated Request */}
                            <button
                                className="text-peach"
                                onClick={() => {
                                    if (pdfUrls.current[doc.id]) {
                                        const a = document.createElement("a");
                                        a.href = pdfUrls.current[doc.id];
                                        a.download = doc.fileName; // Set the file name for download
                                        document.body.appendChild(a);
                                        a.click();
                                        document.body.removeChild(a);
                                    }
                                }}
                            >
                                Download
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Notes;
