import { PrimaryButton, TextField } from "@fluentui/react";
import { useEffect } from "react";
import { useState } from "react";
import { Controller, useForm } from "react-hook-form";
import AssignNodePropPanel from "./AssignNodePropPanel";

// data -> 
// data.props: {id, props}
const CaseDefaultPropsPanel = ({ node, onNodeDataChange, data }) => {
    const { control, handleSubmit, getValues } = useForm({
        defaultValues: {
            expression: data?.expression
        }
    });
    const [variables, setVariables] = useState([]);

    useEffect(() => {
        console.log(data)
    }, [])

    const onVariableDataChange = (varData) => {
        setVariables(varData.variables);
        let props = { id: node.id, variables: varData.variables, expression: getValues("expression") }
        onNodeDataChange(props);
    }

    const onSubmit = data => {
        let props = { id: node.id, variables: variables, expression: data.expression }
        onNodeDataChange(props);
    };

    return (
        <div>
            {node.data.label === "case" && (
                <div>
                    <form style={{ marginTop: '1rem' }}>
                        <div style={{ margin: '1rem 0' }}>
                            <Controller
                                name="expression"
                                control={control}
                                rules={{ required: true }}
                                render={({ field }) => <TextField
                                    label="Condition/Expression"
                                    {...field}
                                    multiline
                                    rows={2}
                                />}
                            />
                        </div>
                        <PrimaryButton text="Set Expression" onClick={handleSubmit(onSubmit)} />
                    </form>
                </div>
            )}
            <AssignNodePropPanel
                node={node}
                onNodeDataChange={onVariableDataChange}
                data={data}
            />
        </div>
    )
}

export default CaseDefaultPropsPanel;
