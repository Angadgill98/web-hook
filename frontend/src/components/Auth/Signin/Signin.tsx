"use client"

import { Api } from '@/Api';
import React, { useRef, useState } from 'react'

let api = Api.getInstance()

const Signin = () => {

    let mail = useRef<HTMLInputElement | null>(null);
    let pass = useRef<HTMLInputElement | null>(null);

    let [is_send, set_send] = useState<boolean>(false)

    return (
        <div className='signup-container'>

            <div className='signup-wrapper'>
                <h3>Sign In</h3>

                <input ref={mail} placeholder='Enter Mail'/>
                <input ref={pass} placeholder='Enter Password'/>

                <button onClick={() => HandleSignIn(api, mail, pass, is_send, set_send)}>
                    Sign In
                </button>

                <span>Sign Up?</span>

            </div>

        </div>
    )
}

export default Signin


async function HandleSignIn(
    api: Api,
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
        let response = await api.auth_api.SingIn(
            mail.current?.value ?? "",
            pass.current?.value ?? ""
        );

        // response received
    } finally {
        set_send(false);
    }
}