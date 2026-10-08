export class Api {
    url: string = "http://localhost:8080/api";

    auth_api: Auth_api = new Auth_api(this.url);
    webhook_api:Webhook_api=new Webhook_api(this.url)

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
                credentials: "include",
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


class Webhook_api {

    url: string;

    constructor(url: string) {
        this.url = url;
    }

    async CreateWebHook(route_name: string, receiver_url: string) {
        let endpoint = `${this.url}/webhook/create`;

        let body = {
            routeName: route_name,
            receiverUrl: receiver_url
        };

        try {
            let response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(body)
            });

            let data = await response.json();

            console.log("API Response Status:", response.status);
            console.log("API Response Data:", data);

            return data;
        } catch (error) {
            console.error("API Exception:", error);

            return null;
        }
    }

    async UpdateReceiverUrl(webhook_id: number, receiver_url: string) {
        let endpoint = `${this.url}/webhook/update-rec-url`;

        let body = {
            webhookId: webhook_id,
            receiverUrl: receiver_url
        };

        try {
            let response = await fetch(endpoint, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(body)
            });

            let data = await response.json();

            console.log("API Response Status:", response.status);
            console.log("API Response Data:", data);

            return data;
        } catch (error) {
            console.error("API Exception:", error);

            return null;
        }
    }

    async UpdateRouteName(webhook_id: number, route_name: string) {
        let endpoint = `${this.url}/webhook/update-hook-name`;

        let body = {
            webhookId: webhook_id,
            routeName: route_name
        };

        try {
            let response = await fetch(endpoint, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(body)
            });

            let data = await response.json();

            console.log("API Response Status:", response.status);
            console.log("API Response Data:", data);

            return data;
        } catch (error) {
            console.error("API Exception:", error);

            return null;
        }
    }

    async DeleteWebHook(webhook_id: number) {
        let endpoint = `${this.url}/webhook/delete-webhook`;

        let body = {
            webhookId: webhook_id
        };

        try {
            let response = await fetch(endpoint, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(body)
            });

            let data = await response.json();

            console.log("API Response Status:", response.status);
            console.log("API Response Data:", data);

            return data;
        } catch (error) {
            console.error("API Exception:", error);

            return null;
        }
    }
}