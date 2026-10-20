"use client";

import { Api } from "@/Api";
import React, { useEffect, useRef, useState } from "react";

let api=Api.getInstance()

const Dashboard = () => {
    let webhook_name = useRef<HTMLInputElement | null>(null);
    let webhook_rec_url = useRef<HTMLInputElement | null>(null);
    let webhook_method = useRef<HTMLSelectElement | null>(null);

    let [is_sending, set_is_sending] = useState<boolean>(false);


    let [webhook_list, setwebhooks]=useState<Webhook[]>([])

    useEffect(() => {
        let get_web_hooks=async()=>{
            let webhook_list = await HandleGetWebHooks();
            setwebhooks(webhook_list);
        }
        get_web_hooks()
    }, [])
    
    return (
        <div className="dashboard-container">
            <div>
                <input ref={webhook_name} placeholder="Enter webhook name" />
                <input ref={webhook_rec_url} placeholder="Enter webhook receiving url" />
                <select ref={webhook_method} defaultValue="POST">
                    <option value="GET">GET</option>
                    <option value="POST">POST</option>
                    <option value="PUT">PUT</option>
                    <option value="DELETE">DELETE</option>
                </select>

                <button onClick={()=>HandleWebHookCreation(is_sending,set_is_sending,webhook_name,webhook_rec_url,webhook_method)} disabled={is_sending}>
                    {is_sending ? "Adding..." : "Add Web Hook"}
                </button>    
            </div>
                
        
            <div>
            {webhook_list != null &&webhook_list.map((webhook) => (<WebhookUrlTemplate webhook={webhook} />))}
        </div>
        
        
        
        </div>
    );
};

export default Dashboard;


const WebhookUrlTemplate =({ webhook }: { webhook: Webhook }) => {
    let [toggle, setoggle] = useState<boolean>(false);

    let handle_info_button = () => {
        setoggle(!toggle);
    };

    let handle_history_button = async () => {
        let history= await GetWebhookHistory(webhook)
    };

    return (
        <li key={webhook.id}>
            <button onClick={handle_info_button}>Info</button>
            <button onClick={handle_history_button}>History</button>

            {toggle && (
                <div>
                    <p>ID: {webhook.id}</p>
                    <p>HTTP Method: {webhook.httpMethod}</p>
                    <p>Latest Event Order: {webhook.latestEventOrder}</p>
                    <p>Receiver Endpoint: {webhook.receiverEndpoint}</p>
                    <p>Route Name: {webhook.routeName}</p>
                    <p>User ID: {webhook.userId}</p>
                    <p>Webhook URL: {webhook.webhookUrl}</p>
                </div>
            )}
        </li>
    );
};



const history_template=()=>{

}


async function HandleWebHookCreation(
    is_sending:boolean,
    set_sending:React.Dispatch<React.SetStateAction<boolean>>,
    webhook_name:React.RefObject<HTMLInputElement | null>,
    webhook_rec_url:React.RefObject<HTMLInputElement | null>,
    http_method:React.RefObject<HTMLSelectElement | null>
){
    if (is_sending) {
        return;
    }

    let name = webhook_name.current?.value ?? "";
    let url = webhook_rec_url.current?.value ?? "";
     let method = http_method.current?.value ?? "POST";
    if (!name || !url) {
        return;
    }

    set_sending(true);

    try {
        let response = await api.webhook_api.CreateWebHook(name, url,method);

        
    } finally {
        set_sending(false);
    }
}

async function HandleGetWebHooks():Promise<Webhook[]>{
    let res =await api.webhook_api.GetAllWebHooks()

    if (!res.success){
        console.log("Failed to get webhook for ht euser ")
        
        return[]
    }

    let webhooks_list:Webhook[] =res.webhooks;
    return webhooks_list
}
export interface Webhook {
    id: number;
    httpMethod: string;
    latestEventOrder: number;
    receiverEndpoint: string;
    routeName: string;
    userId: number;
    webhookUrl: string;
}


async function UpdateReceiverUrl(webhookId: number, receiverUrl: string) {

}

async function UpdateRouteName(webhookId: number, routeName: string) {

}

async function DeleteWebHook(webhookId: number) {

}





async function GetWebhookHistory(webhook:Webhook){
    let response= await api.webhook_api.GetWebhookHistory(webhook.id)
}

export interface WebhookEvent {
    id: string;
    data: Uint8Array | number[];
    webhookId: number;
    userId: number;
    userEventOrder: number;
    arrivalOrder: number;
    tries: number;
    status: string;
}