//lib
import { useEffect, useState } from "react";
import { ActionButton, ComboBox, DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField, Toggle } from "@fluentui/react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { createAction, getApis } from "../ApiHandler";

const AddActionForm = ({ dismissPanel, onUpdate, serviceId }) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const [apis, setApis] = useState([]);
    const { handleSubmit, control, formState: { errors } } = useForm();

    useEffect(() => {
        getApis()
            .then(res => {
                setApis(res.data.map(e => { return { key: e.id, text: e.name } }));
            })
            .catch(e => {

            })
            .finally(() => {

            });
    }, [])

    const onSubmit = data => {
        data.serviceId = serviceId;
        data.apiId = apis.filter(e => e.text === data.apiId)[0].key;
        console.log(data)
        setSubmittingTrue();
        createAction(data)
            .then(res => {
                onUpdate();
                setSubmitDetail({ ...{ type: "success" }, ...res });
                dismissPanel();
            })
            .catch(e => {
                setSubmitDetail({ ...{ type: "error" }, ...e });
            })
            .finally(() => {
                setSubmittingFalse();
            });
    };

    return (
        <div style={{ color: '#e7e7e7' }}>
            <form>
                <div className="form-field">
                    <Controller name="description" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="Action"
                            errorMessage={errors.description && "Action name is required"}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="apiId" control={control} rules={{ required: true }} render={({ field }) =>
                        <ComboBox
                            {...field}
                            // defaultSelectedKey="C"
                            label="Action API"
                            options={apis}
                            allowFreeform={true}
                            autoComplete={'on'}
                            errorMessage={errors.apiId && "Please select an API"}
                        />
                    }
                    />
                </div>

                {submitDetail && (
                    <div className="form-field">
                        <MessageBar
                            messageBarType={submitDetail.type === "success" ? MessageBarType.success : MessageBarType.error}
                            isMultiline={false}
                        >
                            {submitDetail.data}
                        </MessageBar>
                    </div>
                )}
                {/* {errors ? JSON.stringify(errors) : ''} */}
                <div>
                    <PrimaryButton disabled={isSubmitting} onClick={handleSubmit(onSubmit)} iconProps={{ iconName: 'CheckMark' }}>
                        Save
                    </PrimaryButton>
                    <DefaultButton onClick={dismissPanel} style={{ marginLeft: '1rem' }} iconProps={{ iconName: 'CalculatorMultiply' }}>
                        Close
                    </DefaultButton>
                </div>
            </form>
        </div>
    )
}

export default AddActionForm;
