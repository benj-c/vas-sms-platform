import { useState } from "react";
import { Dropdown, PrimaryButton, TextField } from "@fluentui/react";

import { FUNCTIONS } from "../NodeTypes"
import { useCallback } from "react";
import { useEffect } from "react";

const FunctionNodePropPanel = ({ node, onNodeDataChange, data }) => {

    const HttpFunc = ({ node, onNodeDataChange, data }) => {
        const [formData, setFormData] = useState(data?.funcProps || []);
        const [nodeTypeProps, setNodeTypeProps] = useState(FUNCTIONS.find(n => n.functionType === "http"));

        useEffect(() => {
            console.log(data)
        }, [])

        const onSubmit = () => {
            // {field: '', value: ''}
            let finData = formData.filter((v, i, a) => a.findIndex(v2 => (v2.field === v.field)) === i)
            finData.push({field: 'class', value: nodeTypeProps.props.class})
            finData.push({field: 'functionType', value: nodeTypeProps.functionType})
            let props = { id: node.id, funcProps: finData };
            onNodeDataChange(props)
        }

        const onFormChange = useCallback((d) => {
            let isExistingVarUpdate = false;
            setFormData(fields => fields.map(itm => {
                if (d.field == itm.field) {
                    isExistingVarUpdate = true;
                    return { ...itm, ...d };
                }
                return itm;
            }))
            if (!isExistingVarUpdate) {
                setFormData(fields => fields.concat(d));
            }
        }, [node])

        return (
            <div>
                <form style={{ marginTop: '1rem' }}>
                    <div style={{ margin: '1rem 0' }}>
                        <Dropdown
                            defaultSelectedKey={data?.funcProps?.find(i => i.field === 'method')?.value}
                            placeholder="Select a method"
                            label="Method"
                            options={nodeTypeProps.props.methods.map(m => { return { key: m, text: m } })}
                            onChange={(e, o, i) => onFormChange({ field: "method", value: o.key })}
                        />
                    </div>
                    {nodeTypeProps.props.fields.length > 0 && nodeTypeProps.props.fields.map((field, key) => (
                        <div style={{ margin: '1rem 0' }} key={key}>
                            {field.type === "text" && (
                                <TextField
                                    label={field.name}
                                    onChange={(e, o) => onFormChange({ field: field.name, value: o })}
                                    defaultValue={data?.funcProps?.find(i => i.field === field.name)?.value}
                                />
                            )}
                            {field.type === "textarea" && (
                                <TextField
                                    label={field.name}
                                    onChange={(e, o) => onFormChange({ field: field.name, value: o })}
                                    defaultValue={data?.funcProps?.find(i => i.field === field.name)?.value}
                                    multiline
                                    rows={4}
                                />
                            )}

                        </div>
                    ))}
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
