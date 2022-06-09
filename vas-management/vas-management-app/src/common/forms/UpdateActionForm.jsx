//lib
import { useEffect, useState } from "react";
import { ActionButton, ComboBox, DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField, Toggle } from "@fluentui/react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { updateAction, createAction, getApis } from "..//ApiHandler";

const UpdateActionForm = ({ id, description, dismissPanel, onUpdate }) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const [apis, setApis] = useState([]);
    const { handleSubmit, control, formState: { errors } } = useForm({
        defaultValues: {
            description: description
        }
    });

    useEffect(() => {
        getApis()
            .then(res => {
                setApis(res.data.map(e => { return { key: e.id, text: e.name } }));
            })
            .catch(e => {

            })
            .finally(() => {

            });
    }, [id])

    const onSubmit = data => {
        setSubmittingTrue();
        data.id = id;
        updateAction(data)
            .then(res => {
                onUpdate(id);
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
                            errorMessage={errors.name && "Action name is required"}
                        />
                    }
                    />
                </div>
                {/* {!isUpdating && (
                    <div className="form-field">
                        <Controller name="description" control={control} rules={{ required: true }} render={({ field }) =>
                            <ComboBox
                                {...field}
                                // defaultSelectedKey="C"
                                label="Action API"
                                options={apis}
                            />
                        }
                        />
                    </div>
                )} */}

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
                <div>
                    <PrimaryButton disabled={isSubmitting} onClick={handleSubmit(onSubmit)} iconProps={{ iconName: 'CheckMark' }}>
                        Update
                    </PrimaryButton>
                    <DefaultButton onClick={dismissPanel} style={{ marginLeft: '1rem' }} iconProps={{ iconName: 'CalculatorMultiply' }}>
                        Close
                    </DefaultButton>
                </div>
            </form>

            <form>
                <div style={{ border: '1px solid #FFA500', position: 'absolute', bottom: '1rem', right: '1rem', left: '1rem', padding: '0.5rem' }}>
                    <h2>Delete this action</h2>
                    <ActionButton onClick={dismissPanel} style={{ color: '#FFA500', margin: '0.5rem 0' }} iconProps={{ iconName: 'Delete' }}>
                        DELETE
                    </ActionButton>
                    <br />
                    <small>Note that this cannot be undone</small>
                </div>
            </form>
        </div>
    )
}

export default UpdateActionForm;
