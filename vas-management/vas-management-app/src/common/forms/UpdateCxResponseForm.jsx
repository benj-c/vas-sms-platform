//lib
import { useState } from "react";
import { Controller, useForm } from "react-hook-form";
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField } from "@fluentui/react";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { updateCxResponse } from "../ApiHandler";

const UpdateCxResponse = ({ onUpdate, dismissPanel, id, apiId, resCode, resDesc, sms }) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const { handleSubmit, control, formState: { errors } } = useForm({
        defaultValues: {
            resCode: resCode,
            resDesc: resDesc,
            sms: sms,
        }
    });

    const onSubmit = data => {
        let d = { ...data, id: id, apiId: apiId };
        setSubmittingTrue();
        updateCxResponse(d)
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
                    <Controller name="resCode" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="Response code"
                            errorMessage={errors.resCode && "SMS response codeis required"}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="resDesc" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="Response description"
                            errorMessage={errors.resDesc && "Response description is required"}
                            multiline
                            rows={3}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="sms" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="SMS"
                            errorMessage={errors.sms && "SMS is required"}
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
                    <DefaultButton onClick={dismissPanel} style={{ marginLeft: '1rem' }} iconProps={{ iconName: 'CalculatorMultiply' }}>
                        Close
                    </DefaultButton>
                </div>
            </form>
        </div>
    )
}

export default UpdateCxResponse;
