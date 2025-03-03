import React from "react";
import { Link } from "react-router-dom";

const NavBar = () => {
    return (
        <nav className="bg-peach text-black p-4 flex justify-between items-center">
            <h1 className="text-xl font-semibold"><Link to="/">Dashboard</Link></h1>
            <div className="flex gap-4">
                <Link to="/public-groups">View Public Groups</Link>
                <Link to="/create-group">Create a Group</Link>
                <Link to="/logout">Logout</Link>
            </div>
        </nav>
    );
};

export default NavBar;
