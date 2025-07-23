<%@ page import="java.util.List" %>
<%@ page import="model.Content" %>
<%@ page import="dao.ContentDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Content> list = ContentDAO.getAll();
%>

<!-- Content Section Begin -->
<section class="spad" style="background: linear-gradient(135deg, #f8f9fa, #e9ecef);">
    <div class="container">
        <div class="section-title text-center">
            <h2 style="font-weight: 700; letter-spacing: 2px;">CONTENT</h2>
        </div>

        <div class="content-wrapper p-4 bg-white rounded shadow-sm">
            <div id="content-container">
                <%
                    for (int i = 0; i < list.size(); i++) {
                        Content c = list.get(i);
                %>
                <div class="content-item" style="<%= i == 0 ? "" : "display:none;" %>">
                    <h4 class="text-primary"><%= c.getTitle() %></h4>
                    <p><%= c.getBody().replaceAll("\n", "<br/>") %></p>
                    <hr/>
                </div>
                <%
                    }
                %>
            </div>

            <div class="d-flex justify-content-between mt-3">
                <button onclick="showPrevious()" class="btn btn-outline-primary">&larr; Previous</button>
                <button onclick="showNext()" class="btn btn-outline-primary">Next &rarr;</button>
            </div>
        </div>
    </div>
</section>
<!-- Content Section End -->

<script>
    let currentIndex = 0;
    const items = document.querySelectorAll('.content-item');

    function show(index) {
        if (index >= 0 && index < items.length) {
            items[currentIndex].style.display = 'none';
            items[index].style.display = 'block';
            currentIndex = index;
        }
    }

    function showNext() {
        const nextIndex = (currentIndex + 1) % items.length;
        show(nextIndex);
    }

    function showPrevious() {
        const prevIndex = (currentIndex - 1 + items.length) % items.length;
        show(prevIndex);
    }
</script>
