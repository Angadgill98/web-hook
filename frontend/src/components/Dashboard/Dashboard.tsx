"use client";

import { Api } from "@/Api";
import React, { useEffect, useRef, useState } from "react";

let api=Api.getInstance()

const Dashboard = () => {
    let webhook_name = useRef<HTMLInputElement | null>(null);
    let webhook_rec_url = useRef<HTMLInputElement | null>(null);

    let [is_sending, set_is_sending] = useState<boolean>(false);

    useEffect(() => {
        let get_web_hooks=()=>{GetWebHooks()}


        get_web_hooks()
    
    }, [])
    
    return (
        <div className="dashboard-container">
            <div>
                <input ref={webhook_name} placeholder="Enter webhook name" />
                <input ref={webhook_rec_url} placeholder="Enter webhook receiving url" />

                <button onClick={()=>HandleWebHookCreation(is_sending,set_is_sending,webhook_name,webhook_rec_url)} disabled={is_sending}>
                    {is_sending ? "Adding..." : "Add Web Hook"}
                </button>    
            </div>
                
        
            <div>

            </div>
        
        
        
        </div>
    );
};

export default Dashboard;






async function HandleWebHookCreation(
    is_sending:boolean,
    set_sending:React.Dispatch<React.SetStateAction<boolean>>,
    webhook_name:React.RefObject<HTMLInputElement | null>,
    webhook_rec_url:React.RefObject<HTMLInputElement | null>
){
    if (is_sending) {
        return;
    }

    let name = webhook_name.current?.value ?? "";
    let url = webhook_rec_url.current?.value ?? "";

    if (!name || !url) {
        return;
    }

    set_sending(true);

    try {
        let response = await api.webhook_api.CreateWebHook(name, url);

        
    } finally {
        set_sending(false);
    }
}

async function CreateWebHook(webhook_name: string, url: string) {
    
}



async function UpdateReceiverUrl(webhookId: number, receiverUrl: string) {

}

async function UpdateRouteName(webhookId: number, routeName: string) {

}

async function DeleteWebHook(webhookId: number) {

}


async function GetWebHooks(){

}