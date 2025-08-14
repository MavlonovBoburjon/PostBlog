// --- Confirm Delete ---
document.addEventListener("DOMContentLoaded", function () {
    const deleteForms = document.querySelectorAll("form.delete-form, form[onsubmit]");

    deleteForms.forEach(form => {
        form.addEventListener("submit", function (e) {
            const confirmed = confirm("Are you sure you want to delete this item?");
            if (!confirmed) e.preventDefault();
        });
    });

    // --- Highlight current page link ---
    const currentPage = document.querySelector(".current");
    if (currentPage) {
        currentPage.style.backgroundColor = "#3498db";
        currentPage.style.color = "white";
    }
});
