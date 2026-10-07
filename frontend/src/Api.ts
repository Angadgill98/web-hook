export class Api {
    url: string = "http://localhost:8080/api";

    auth_api: Auth_api = new Auth_api(this.url);

    private static instance: Api;

    private constructor() {}

    static getInstance() {
        if (!Api.instance) {
            Api.instance = new Api();
        }

        return Api.instance;
    }
}

class Auth_api {
    url: string;

    constructor(url: string) {
        this.url = url;
    }

    async SignUp(name: string, mail: string, pass: string) {
        let endpoint = `${this.url}/auth/sign-up`;

        let body = {
            name,
            mail,
            pass
        };

        console.log("API Endpoint:", endpoint);
        console.log("API Body:", body);

        try {
            let response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            console.log("API Response Object:", response);
            console.log("API Response Status:", response.status);

            let data = await response.json();

            console.log("API Response Data:", data);

            if (response.ok) {
                console.log("API Request Successful");
            } else {
                console.log("API Request Failed");
            }

            return data;
        } catch (error) {
            console.error("API Exception:", error);

            return null;
        }
    }

    async SingIn(mail: string, pass: string) {
        let endpoint = `${this.url}/auth/sign-in`;

        let body = {
            mail,
            pass
        };

        console.log("API Endpoint:", endpoint);
        console.log("API Body:", body);

        try {
            let response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            console.log("API Response Object:", response);
            console.log("API Response Status:", response.status);

            let data = await response.json();

            console.log("API Response Data:", data);

            if (response.ok) {
                console.log("API Request Successful");
            } else {
                console.log("API Request Failed");
            }

            return data;
        } catch (error) {
            console.error("API Exception:", error);

            return null;
        }
    }
}