import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Signup = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        setLoading(true);
        try {
            const response = await fetch("http://localhost:8080/auth/signup", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, email, password }),
            });

            const data = await response.json();
            if (response.ok) {
                localStorage.setItem("userId", data.userId);
                localStorage.setItem("token", data.token);
                navigate("/");
            } else {
                setError(data.errorMessage || "Signup failed");
            }
        } catch {
            setError("An error occurred. Please try again.");
        }
        setLoading(false);
    };

    return (
        <div className="form-container">
            <div className="main-form">
                <h2 className="form-title">Signup</h2>
                <form onSubmit={handleSubmit} className="form">
                    <div>
                        <label className="form-label">Username:</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            className="form-input"
                        />
                    </div>
                    <div>
                        <label className="form-label">Email:</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className="form-input"
                        />
                    </div>
                    <div>
                        <label className="form-label">Password:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="form-input"
                        />
                    </div>
                    <div>
                        <label className="form-label">Confirm Password:</label>
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                            className="form-input"
                        />
                    </div>
                    {error && <p className="form-error">{error}</p>}
                    <button type="submit" className="form-button" disabled={loading}>
                        {loading ? "Signing up..." : "Sign Up"}
                    </button>
                    <p className="text-gray-600">
                        Already have an account?{" "}
                        <Link to="/login" className="text-peach underline">
                            Login
                        </Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default Signup;