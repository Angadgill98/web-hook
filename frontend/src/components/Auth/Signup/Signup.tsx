"use client"

import { Api } from '@/Api';
import React, { useRef, useState } from 'react'

let api = Api.getInstance()

const Signup = () => {

    let name = useRef<HTMLInputElement | null>(null);
    let mail = useRef<HTMLInputElement | null>(null);
    let pass = useRef<HTMLInputElement | null>(null);

    let [is_send, set_send] = useState<boolean>(false)

    return (
        <div className='signup-container'>

            <div className='signup-wrapper'>
                <h3>Sign Up</h3>

                <input ref={name} placeholder='Enter Username'/>
                <input ref={mail} placeholder='Enter Mail'/>
                <input ref={pass} placeholder='Enter Password'/>

                <button onClick={() => HandleSignUp(api, name, mail, pass, is_send, set_send)}>
                    Sign Up
                </button>

                <span>Sign In?</span>

            </div>

        </div>
    )
}

export default Signup


async function HandleSignUp(
    api: Api,
    name: React.RefObject<HTMLInputElement | null>,
    mail: React.RefObject<HTMLInputElement | null>,
    pass: React.RefObject<HTMLInputElement | null>,
    is_send: boolean,
    set_send: React.Dispatch<React.SetStateAction<boolean>>
) {
    if (is_send) {
        return;
    }

    set_send(true);

    try {
        let response = await api.auth_api.SignUp(
            name.current?.value ?? "",
            mail.current?.value ?? "",
            pass.current?.value ?? ""
        );

        // response received
    } finally {
        set_send(false);
    }
}