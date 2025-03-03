import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard';
import PrivateRoute from './components/PrivateRoute';
import Logout from "./components/Logout";
import StudyGroup from "./components/StudyGroup";
import PublicGroups from "./components/PublicGroups.jsx";
import CreateStudyGroup from "./components/CreateStudyGroup.jsx";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<PrivateRoute element={<Dashboard />} />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/study-group/:studyGroupId" element={<PrivateRoute element={<StudyGroup />} />} />
                <Route path="/public-groups" element={<PrivateRoute element={<PublicGroups />} />} />
                <Route path="/create-group" element={<PrivateRoute element={<CreateStudyGroup />} />} />
            </Routes>
        </Router>
    );
};

export default App;
