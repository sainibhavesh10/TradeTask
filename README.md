# TradeTask - Backend API

TradeTask is a high-performance financial backend built with **Spring Boot**. It provides a robust set of RESTful services for real-time stock market tracking, secure user authentication, and personalized portfolio management.

## 🛠️ Technical Core

### 🔐 Security & Authentication
* **JWT Integration:** Implemented **Spring Security** with JSON Web Tokens for stateless authentication, ensuring secure communication between the client and server.
* **OTP Verification:** Integrated an SMTP-based **Email Service** for one-time password (OTP) verification during user registration to validate user identity.
* **Role-Based Control:** Administrative endpoints are strictly secured to allow centralized data refreshes, preventing unauthorized data manipulation while ensuring system-wide consistency.

### 📊 Financial Logic
* **Portfolio Management:** Sophisticated logic for calculating real-time **Profit and Loss (P&L)** by comparing live market prices against user-specific purchase prices.
* **Watchlist Service:** Optimized backend support for personalized user watchlists, utilizing efficient CRUD operations for high-frequency updates.
* **Market Data Aggregation:** Dedicated service layers designed to fetch and cache stock data, instantly identifying top gainers, losers, and 52-week price extremes.

### 🏗️ Backend Architecture
* **Global Exception Handling:** Utilizes a centralized `@ControllerAdvice` to intercept errors and return consistent, developer-friendly REST responses.
* **Database Persistence:** Leverages **JPA/Hibernate** with **MySQL** for robust relational data mapping between users, stocks, and portfolios.
* **Logging:** Integrated logging throughout the service layer for monitoring transaction flows and debugging security triggers in real-time.

---

## 🛡️ API Documentation

### 1. Authentication (`/auth`)
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/auth/signup` | `POST` | Registers a new user and triggers an OTP email. |
| `/auth/signin` | `POST` | Validates credentials and returns a JWT bearer token. |
| `/auth/verify-otp` | `POST` | Validates the OTP code to activate the user account. |

### 2. Market Data (`/api/stocks`)
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/stocks` | `GET` | Fetches a list of all stocks with current pricing. |
| `/api/stocks/{id}` | `GET` | Returns detailed info (High/Low, Vol) for a specific ticker. |
| `/api/stocks/top-movers` | `GET` | Lists the highest gainers and losers in the market. |
| `/api/admin/refresh` | `POST` | **(Admin Only)** Syncs the DB with live market prices. |

### 3. User Portfolio & Watchlist (`/api/user`)
*These endpoints require a valid JWT in the Authorization header.*

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/portfolio` | `GET` | Retrieves the user's holdings and total P&L. |
| `/api/portfolio/add` | `POST` | Records a new stock purchase into the user's portfolio. |
| `/api/watchlist` | `GET` | Fetches the user's saved/favorited stocks. |
| `/api/watchlist/add/{id}`| `POST` | Adds a ticker to the user's personal watchlist. |

---

## ⚙️ Setup & Installation

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/sainibhavesh10/TradeTask.git](https://github.com/sainibhavesh10/TradeTask.git)
   cd TradeTask
