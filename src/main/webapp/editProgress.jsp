<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Progress" %>
<%
    Progress progress = (Progress) request.getAttribute("progress");
%>
<%
    String recordedAtStr = "";
    if (progress.getRecordedAt() != null) {
        recordedAtStr = progress.getRecordedAt().toString().replace(" ", "T");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Progress</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .edit-container {
                max-width: 600px;
                margin: 60px auto;
                background-color: #ffffff;
                padding: 30px;
                border-radius: 15px;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
            }
            .form-label {
                font-weight: 500;
            }
            h2 {
                margin-bottom: 30px;
                font-weight: bold;
                color: #0d6efd;
            }
        </style>
    </head>
    <body>
        <div class="container edit-container">
            <h2 class="text-center"><i class="bi bi-pencil-square"></i> Edit Progress</h2>
            <form action="editProgress" method="post">
                <input type="hidden" name="id" value="<%= progress.getProgressID() %>" />

                <div class="mb-3">
                    <label class="form-label">Date Recorded</label>
                    <input type="datetime-local" class="form-control" name="recordedAt"
                           value="<%= recordedAtStr %>" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Weight (kg)</label>
                    <input type="number" step="0.1" class="form-control" name="weight" value="<%= progress.getWeight() %>" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Body Fat (%)</label>
                    <input type="number" step="0.1" class="form-control" name="bodyFatPercent" value="<%= progress.getBodyFatPercent() %>" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Muscle Mass (kg)</label>
                    <input type="number" step="0.1" class="form-control" name="muscleMass" value="<%= progress.getMuscleMass() %>" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Notes</label>
                    <textarea class="form-control" name="notes" rows="4"><%= progress.getNotes() != null ? progress.getNotes() : "" %></textarea>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="progress" class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left-circle"></i> Cancel
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-save"></i> Save Changes
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>
