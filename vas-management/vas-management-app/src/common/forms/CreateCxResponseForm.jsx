//lib
import { useState } from "react";
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField } from "@fluentui/react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { createCxResponse } from "../ApiHandler";
import { selectedActionAtom } from "../../state/atoms"
import { useRecoilValue } from "recoil";

const CreateCxResponseForm = ({ onUpdate, dismissPanel }) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const { handleSubmit, control, formState: { errors } } = useForm();
    //global state
    const selectedAction = useRecoilValue(selectedActionAtom);

    const onSubmit = data => {
        let d = { ...data, apiId: selectedAction.apiId };
        setSubmittingTrue();
        createCxResponse(d)
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
                            errorMessage={errors.name && "SMS response codeis required"}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="resDesc" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="Response description"
                            errorMessage={errors.name && "Response description is required"}
                            multiline
                            rows={4}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="sms" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="SMS"
                            errorMessage={errors.name && "SMS is required"}
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

export default CreateCxResponseForm;
