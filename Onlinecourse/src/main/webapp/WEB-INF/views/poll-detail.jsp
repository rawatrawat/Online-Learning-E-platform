<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Online Course</title>
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
        <!-- profile and Logout -->
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


<!-- Poll Detail Content -->
<c:set var="userVote" value="${poll.getUserVote(user)}" />
<div class="container mt-4">
    <h1>${poll.question}</h1>
    <c:if test="${not empty errors}">
        <div class="alert alert-danger">${errors}</div>
    </c:if>
    <div class="voting-section mb-4">
    <c:choose>
        <c:when test="${userVote == null}">
            <!-- Poll Option -->
            <form action="/poll/${poll.id}/vote" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <c:forEach items="${poll.options}" var="option" varStatus="status">
                    <div class="form-check">
                        <input class="form-check-input" type="radio"
                               name="optionIndex" id="option${status.index}"
                               value="${status.index}" required>
                        <label class="form-check-label" for="option${status.index}">
                                ${option}
                        </label>
                    </div>
                </c:forEach>
                <button type="submit" class="btn btn-primary mt-2">Vote</button>
            </form>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                You voted for: <strong>${poll.options[userVote.optionIndex]}</strong>
                <form action="/poll/${poll.id}/remove-vote" method="post" class="mt-2">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
        </c:otherwise>
    </c:choose>
    <h2>Results</h2>
    <ul class="list-group">
        <c:forEach items="${poll.options}" var="option" varStatus="status">
            <li class="list-group-item d-flex justify-content-between align-items-center">
                    ${option}
                <span class="badge badge-primary badge-pill">${poll.votes[status.index]}</span>
                <span class="badge badge-secondary badge-pill">${poll.getVotePercentages()[status.index]}%</span>
            </li>
        </c:forEach>
    </ul>
    <!-- Remove Vote Button -->
    <c:set var="userVote" value="${poll.getUserVote(user)}" />
    <c:if test="${userVote != null}">
        <form action="/poll/${poll.id}/remove-vote" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="optionIndex" value="${userVote.optionIndex}">
            <button type="submit" class="btn btn-danger">Remove My Vote</button>
        </form>
    </c:if>

    <!-- Comments Section -->
    <h2>Comments</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <form action="/poll/${poll.id}/comment" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <div class="form-group">
            <textarea class="form-control" name="text" rows="3" placeholder="Leave a comment..."></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Post Comment</button>
    </form>
    <c:if test="${not empty poll.comments}">
        <ul class="list-group mt-3">
            <c:forEach items="${poll.comments}" var="comment">
                <li class="list-group-item">
                    <strong>${comment.user.fullName}</strong> (${comment.timestamp})
                    <p>${comment.text}</p>
                    <c:if test="${user.role == 'TEACHER'}">
                        <form action="/poll/${poll.id}/comment/${comment.id}/delete" method="post" style="display:inline;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this comment?')">Delete</button>
                        </form>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </c:if>



<!-- Delete Button -->
    <c:if test="${user.role == 'TEACHER'}">
        <a href="/poll/${poll.id}/delete" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this poll?')">Delete Poll</a>
    </c:if>
</div>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>