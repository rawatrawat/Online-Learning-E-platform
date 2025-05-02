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


<!-- Voting History Content -->
<div class="container mt-4">
    <h1>Voting History</h1>
    <c:if test="${not empty votingHistory}">
        <table class="table">
            <thead>
            <tr>
                <th>Poll Question</th>
                <th>Option Voted</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${votingHistory}" var="vote">
                <tr>
                    <td>${vote.poll.question}</td>
                    <td>${vote.poll.options[vote.optionIndex]}</td>
                    <td>${vote.timestamp}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty votingHistory}">
        <div class="alert alert-info">
            You have not voted on any polls yet.
            <a href="/poll/" class="alert-link">Browse polls</a>
        </div>
    </c:if>

</div>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>