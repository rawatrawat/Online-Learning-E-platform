<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Voting History</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Online Course</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav mr-auto">
            <!-- Course -->
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="courseDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Course
                </a>
                <div class="dropdown-menu" aria-labelledby="courseDropdown">
                    <c:forEach items="${courses}" var="course">
                        <a class="dropdown-item" href="/courseMaterial?courseId=${course.id}">${course.name}</a>
                    </c:forEach>
                </div>
            </li>
            <!-- Poll -->
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="pollDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Poll
                </a>
                <div class="dropdown-menu" aria-labelledby="pollDropdown">
                    <c:forEach items="${polls}" var="poll">
                        <a class="dropdown-item" href="/poll/${poll.id}">${poll.question}</a>
                    </c:forEach>
                </div>
            </li>
        </ul>
        <!-- User Info -->
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="/profile">Profile</a>
            </li>
            <li class="nav-item">
                <form action="/logout" method="post" style="display: inline;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="nav-link btn btn-link">Logout</button>
                </form>
            </li>
        </ul>
    </div>
</nav>

<!-- Course Comment History Content -->
<div class="container mt-4">
    <h1>Course Comment History</h1>
    <c:if test="${not empty courseCommentHistory}">
        <table class="table">
            <thead>
            <tr>
                <th>Course Lecture</th>
                <th>Course Comment</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${courseCommentHistory}" var="courseComment">
                <tr>
                    <td>${courseComment.lectureId}</td>
                    <td>${courseComment.content}</td>
                    <td>${courseComment.timestamp}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty courseCommentHistory}">
        <p>You have not commented on any course yet.</p>
    </c:if>

</div>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>