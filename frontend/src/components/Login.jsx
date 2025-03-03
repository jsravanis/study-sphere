import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            const data = await response.json();
            if (response.ok) {
                localStorage.setItem("userId", data.userId);
                localStorage.setItem("token", data.token);
                navigate("/");
            } else {
                setError(data.errorMessage || "Login failed");
            }
        } catch {
            setError("An error occurred. Please try again.");
        }
        setLoading(false);
    };

    return (
        <div className="form-container">
            <div className="main-form">
                <h2 className="form-title">Login</h2>
                <form onSubmit={handleSubmit} className="form">
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
                    {error && <p className="form-error">{error}</p>}
                    <button type="submit" className="form-button" disabled={loading}>
                        {loading ? "Logging in..." : "Login"}
                    </button>
                    <p className="text-gray-600">
                        Don't have an account?{" "}
                        <Link to="/signup" className="text-peach underline">
                            Sign up
                        </Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default Login;