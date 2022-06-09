//lib
import { useState } from "react";
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField, Toggle } from "@fluentui/react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { updateService } from "../ApiHandler";

const UpdateServiceForm = ({id, name, description, active, disabledSms, dismissPanel, onUpdate}) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const { handleSubmit, control, formState: { errors } } = useForm({
        defaultValues: {
            name: name,
            description: description,
            active: active,
            disableSms: disabledSms
        }
    });

    const onSubmit = data => {
        let d = { ...data, id: id };
        setSubmittingTrue();
        updateService(d)
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
                            errorMessage={errors.description && "Service description is required"}
                            multiline
                            rows={4}
                        />}
                    />
                </div>
                <div className="form-field">
                    <Controller name="active" control={control} render={({ field }) =>
                        <Toggle
                            {...field}
                            label="Service availability"
                            onText="Active"
                            offText="Inactive"
                            defaultChecked={active}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="disableSms" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="SMS to send if the service is disabled"
                            errorMessage={errors.disableSms && "SMS is required"}
                        />
                    }
                    />
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
                </div>
                <div style={{ marginTop: '1rem', position: 'fixed', bottom: '1rem' }}>
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

export default UpdateServiceForm;
