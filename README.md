### Endpoints Table

| Endpoint                                    | Method | Params/Body                                        | Requires Auth | Response Codes                              | Description                                                                                 |
|---------------------------------------------|--------|----------------------------------------------------|---------------|---------------------------------------------|---------------------------------------------------------------------------------------------|
| `/api/users/register`                       | POST   | `{ name, password, email }`                        | No            | 200, 400 ("Email already exists")           | Registers a new user.                                                                       |
| `/api/users/login`                          | POST   | `{ identifier, password }`                         | No            | 200, 401 ("Bad credentials")                | Logs in the user and returns a JWT token.                                                   |
| `/api/dashboard/user`                       | GET    | N/A                                               | Yes           | 200, 401 ("Access Denied")                  | Retrieves the logged-in user's details.                                                     |
| `/api/dashboard/account`                    | GET    | N/A                                               | Yes           | 200, 401 ("Access Denied")                  | Retrieves the main account information, including balance.                                  |
| `/api/dashboard/account/{index}`            | GET    | `{ index }` (Path Parameter)                      | Yes           | 200, 401, 404                               | Retrieves account information for a specific account by its index (e.g., 0 for main account).|
| `/api/account/create`                       | POST   | `{ accountNumber, accountType }`                  | Yes           | 200, 400                                   | Creates a new account for the user using the main account number and specifying account type.|
| `/api/account/deposit`                      | POST   | `{ amount }`                                      | Yes           | 200, 401                               | Deposits a specific amount into the user's account, with applicable fees.                   |
| `/api/account/withdraw`                     | POST   | `{ amount }`                                      | Yes           | 200, 401                               | Withdraws a specific amount from the user's account, with applicable fees.                  |
| `/api/account/fund-transfer`                | POST   | `{ targetAccountNumber, amount }`                 | Yes           | 200, 401                               | Transfers funds to another account, with fraud detection if applicable.                     |
| `/api/account/transactions`                 | GET    | N/A                                               | Yes           | 200, 401                                   | Retrieves the user's transaction history, including any flagged as fraud.                   |
| `/api/users/logout`                         | GET    | N/A                                               | Yes           | 200, 401 ("Access Denied")                  | Logs out the user and invalidates the JWT token.                                            |

















