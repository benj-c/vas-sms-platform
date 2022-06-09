//lib
import { useState } from "react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField } from "@fluentui/react";
import { useHistory } from "react-router-dom";
//app
import { createService } from "../../common/ApiHandler"

const CreateServiceForm = ({onDismiss}) => {
    const history = useHistory();
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const { handleSubmit, control, formState: { errors } } = useForm();

    const onSubmit = data => {
        setSubmittingTrue();
        createService(data)
            .then(res => {
                // setSubmitDetail({ ...{ type: "success" }, ...res });
                history.push(`/builder?ref=${res.data}`)
            })
            .catch(e => {
                setSubmitDetail({ ...{ type: "error" }, ...e });
            })
            .finally(() => {
                setSubmittingFalse();
            });
    };

    return (
        <form>
            <div className="form-field">
                <Controller name="name" control={control} rules={{ required: true }} render={({ field }) =>
                    <TextField
                        {...field}
                        label="Service name"
                        errorMessage={errors.name && "Service name is required"}
                    />
                }
                />
            </div>
            <div className="form-field">
                <Controller name="description" control={control} rules={{ required: true }} render={({ field }) =>
                    <TextField
                        {...field}
                        label="Description"
                        errorMessage={errors.description && "Description is required"}
                        multiline
                        rows={4}
                    />
                }
                />
            </div>
            <div className="form-field">
                <Controller name="disabledSms" control={control} rules={{ required: true }} render={({ field }) =>
                    <TextField
                        {...field}
                        label="SMS to send if the service is not available"
                        errorMessage={errors.disabledSms && "SMS is required"}
                        multiline
                        rows={4}
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
            <div>
                <PrimaryButton disabled={isSubmitting} onClick={handleSubmit(onSubmit)} iconProps={{ iconName: 'CheckMark' }}>
                    Save
                </PrimaryButton>
                <DefaultButton onClick={onDismiss} style={{ marginLeft: '1rem' }} iconProps={{ iconName: 'CalculatorMultiply' }}>
                    Cancel
                </DefaultButton>
            </div>
        </form>
    );
}

export default CreateServiceForm;
