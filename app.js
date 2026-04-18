const API = "http://localhost:8000";
let currentUserRole = "guest";

function setText(id, text) {
  const el = document.getElementById(id);
  if (el) {
    el.innerText = text;
  }
}

function getQuantity() {
  const quantityInput = document.getElementById("ticketQuantity");
  if (!quantityInput) {
    return 1;
  }

  const quantity = parseInt(quantityInput.value, 10);
  if (isNaN(quantity) || quantity < 1) {
    return 1;
  }

  return quantity;
}

function showTab(tabId, buttonEl) {
  const panels = document.querySelectorAll(".tab-panel");
  panels.forEach(panel => panel.classList.remove("active-panel"));

  const buttons = document.querySelectorAll(".tab-btn");
  buttons.forEach(btn => btn.classList.remove("active"));

  const target = document.getElementById(tabId);
  if (target) {
    target.classList.add("active-panel");
  }

  if (buttonEl) {
    buttonEl.classList.add("active");
  }
}

function enableManagerReport() {
  const reportButton = document.getElementById("reportTabButton");
  if (reportButton) {
    reportButton.style.display = "inline-flex";
  }
}

function disableManagerReport() {
  const reportButton = document.getElementById("reportTabButton");
  if (reportButton) {
    reportButton.style.display = "none";
  }

  const reportPanel = document.getElementById("reportTab");
  if (reportPanel && reportPanel.classList.contains("active-panel")) {
    const loginButton = document.querySelector(".tab-btn");
    showTab("loginTab", loginButton);
  }
}

function login() {
  const usernameInput = document.getElementById("username");
  const passwordInput = document.getElementById("password");

  if (!usernameInput || !passwordInput) {
    return;
  }

  const username = usernameInput.value.trim();
  const password = passwordInput.value.trim();

  fetch(API + "/login", {
    method: "POST",
    body: username + "|" + password
  })
    .then(res => res.text())
    .then(data => {
      setText("loginResult", data);

      if (data === "Login success") {
        if (username.toLowerCase() === "admin") {
          currentUserRole = "manager";
          enableManagerReport();
        } else {
          currentUserRole = "customer";
          disableManagerReport();
        }

        const moviesButton = document.querySelectorAll(".tab-btn")[1];
        showTab("moviesTab", moviesButton);
      } else {
        currentUserRole = "guest";
        disableManagerReport();
      }
    })
    .catch(() => {
      setText("loginResult", "Server not running");
    });
}

function createMovieRow(movieText) {
  const parts = movieText.split("|");
  if (parts.length < 4) {
    return null;
  }

  const id = parts[0];
  const title = parts[1];
  const showtime = parts[2];
  const price = parts[3];

  const row = document.createElement("div");
  row.className = "movie-row";
  row.innerHTML = `
    <div>
      <strong>${title}</strong><br>
      <small>${showtime} • $${price}</small>
    </div>
    <button class="btn btn--ghost" onclick="addMovieToCart('${id}|${title}|${showtime}|${price}')">Add to Cart</button>
  `;

  return row;
}

function loadMovies() {
  fetch(API + "/movies")
    .then(res => res.text())
    .then(data => {
      const movieBox = document.getElementById("movies");
      if (!movieBox) {
        return;
      }

      movieBox.innerHTML = "";

      if (!data.trim()) {
        movieBox.innerText = "No movies found.";
        return;
      }

      const movies = data.split(",");
      movies.forEach(movieText => {
        const row = createMovieRow(movieText);
        if (row) {
          movieBox.appendChild(row);
        }
      });
    })
    .catch(() => {
      setText("movies", "Server not running");
    });
}

function searchMovies() {
  const input = document.getElementById("searchInput");
  if (!input) {
    return;
  }

  const searchText = input.value.trim().toLowerCase();

  fetch(API + "/movies")
    .then(res => res.text())
    .then(data => {
      const movieBox = document.getElementById("movies");
      if (!movieBox) {
        return;
      }

      movieBox.innerHTML = "";

      if (!data.trim()) {
        movieBox.innerText = "No movies found.";
        return;
      }

      const movies = data.split(",");
      const filtered = movies.filter(movieText =>
        movieText.toLowerCase().includes(searchText)
      );

      if (filtered.length === 0) {
        movieBox.innerText = "No movie found.";
        return;
      }

      filtered.forEach(movieText => {
        const row = createMovieRow(movieText);
        if (row) {
          movieBox.appendChild(row);
        }
      });
    })
    .catch(() => {
      setText("movies", "Server not running");
    });
}

function addMovieToCart(movieLine) {
  const quantity = getQuantity();
  const fullMovieLine = movieLine + "|" + quantity;

  fetch(API + "/cart/add", {
    method: "POST",
    body: fullMovieLine
  })
    .then(res => res.text())
    .then(data => {
      setText("cartMessage", data + " (" + quantity + " ticket(s))");
      viewCart();
      const cartButton = document.querySelectorAll(".tab-btn")[2];
      showTab("cartTab", cartButton);
    })
    .catch(() => {
      setText("cartMessage", "Server not running");
    });
}

function viewCart() {
  fetch(API + "/cart/view")
    .then(res => res.text())
    .then(data => {
      const cartBox = document.getElementById("cartOutput");
      if (!cartBox) {
        return;
      }

      cartBox.innerHTML = "";

      if (!data.trim()) {
        cartBox.innerText = "Cart is empty.";
        return;
      }

      const items = data.split(",");
      items.forEach((line, index) => {
        const parts = line.split("|");
        if (parts.length < 5) {
          return;
        }

        const title = parts[1];
        const showtime = parts[2];
        const price = parts[3];
        const quantity = parts[4];

        const row = document.createElement("div");
        row.className = "booking-row";
        row.innerHTML = `
          <div>
            <strong>${title}</strong><br>
            <small>${showtime} • $${price} • Qty: ${quantity}</small>
          </div>
          <button class="btn btn--ghost" onclick="removeCartItem(${index})">Remove</button>
        `;
        cartBox.appendChild(row);
      });
    })
    .catch(() => {
      setText("cartOutput", "Server not running");
    });
}

function removeCartItem(index) {
  fetch(API + "/cart/remove", {
    method: "POST",
    body: String(index)
  })
    .then(res => res.text())
    .then(data => {
      setText("cartMessage", data);
      viewCart();
    })
    .catch(() => {
      setText("cartMessage", "Server not running");
    });
}

function createBookings() {
  fetch(API + "/bookings/create", {
    method: "POST"
  })
    .then(res => res.text())
    .then(data => {
      setText("bookingMessage", data);
      viewCart();
      viewBookings();
      report();
      const bookingsButton = document.querySelectorAll(".tab-btn")[3];
      showTab("bookingsTab", bookingsButton);
    })
    .catch(() => {
      setText("bookingMessage", "Server not running");
    });
}

function viewBookings() {
  fetch(API + "/bookings/view")
    .then(res => res.text())
    .then(data => {
      const bookingBox = document.getElementById("bookings");
      if (!bookingBox) {
        return;
      }

      bookingBox.innerHTML = "";

      if (!data.trim()) {
        bookingBox.innerText = "No bookings yet.";
        return;
      }

      const bookings = data.split(",");
      bookings.forEach(line => {
        const parts = line.split("|");
        if (parts.length < 7) {
          return;
        }

        const bookingId = parts[0];
        const title = parts[2];
        const showtime = parts[3];
        const price = parts[4];
        const quantity = parts[5];
        const status = parts[6];

        const row = document.createElement("div");
        row.className = "booking-row";
        row.innerHTML = `
          <div>
            <strong>Booking #${bookingId} — ${title}</strong><br>
            <small>${showtime} • $${price} • Qty: ${quantity} • ${status}</small>
          </div>
          <button class="btn btn--ghost" onclick="cancelBooking(${bookingId})">Cancel</button>
        `;
        bookingBox.appendChild(row);
      });
    })
    .catch(() => {
      setText("bookings", "Server not running");
    });
}

function cancelBooking(bookingId) {
  fetch(API + "/bookings/cancel", {
    method: "POST",
    body: String(bookingId)
  })
    .then(res => res.text())
    .then(data => {
      setText("bookingMessage", data);
      viewBookings();
      report();
    })
    .catch(() => {
      setText("bookingMessage", "Server not running");
    });
}

function report() {
  if (currentUserRole !== "manager") {
    setText("reportOutput", "Manager access only");
    return;
  }

  fetch(API + "/report")
    .then(res => res.text())
    .then(data => {
      const reportBox = document.getElementById("reportOutput");
      if (reportBox) {
        reportBox.innerText = data;
      }
    })
    .catch(() => {
      setText("reportOutput", "Server not running");
    });
}

window.addEventListener("DOMContentLoaded", () => {
  disableManagerReport();
  loadMovies();
  viewCart();
  viewBookings();
});