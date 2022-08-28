import { useState, useRef, useEffect } from "react";
import { Controller, useForm } from "react-hook-form";
import { Dropdown, PrimaryButton } from "@fluentui/react";

import { FUNCTIONS } from "../NodeTypes"

const FunctionNodePropPanel = ({ node, onNodeDataChange, data }) => {

    const HttpFunc = ({ node, onNodeDataChange, data }) => {
        useRef
        const { control, handleSubmit, getValues, setValue } = useForm({
            defaultValues: {
                class: data?.class,
                // method: data?.method,
            }
        });
        const [nodeTypeProps, setNodeTypeProps] = useState(FUNCTIONS.find(n => n.functionType === "http"));

        useEffect(() => {
            // setVa
        }, [])

        const onSubmit = formData => {
            // formData = { ...formData, class: 'rest' }
            console.log(formData)
        }

        return (
            <div>
                <form style={{ marginTop: '1rem' }} >
                    <div style={{ margin: '1rem 0' }}>
                        <Dropdown
                            defaultSelectedKey={data.method}
                            placeholder="Select a method"
                            label="Method"
                            options={nodeTypeProps.methods.map(m => { return { key: m, text: m } })}
                        />
                    </div>
                    <PrimaryButton text="Set" onClick={onSubmit} />
                </form>
            </div>
        );
    }

    return (
        <div>
            {node.data.functionType === "http" && (
                <HttpFunc node={node} data={data} onNodeDataChange={onNodeDataChange} />
            )}
        </div>
    )
}

export default FunctionNodePropPanel;
