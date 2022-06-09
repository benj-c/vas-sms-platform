//lib
import { useState } from "react";
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField } from "@fluentui/react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { createKeyword } from "../ApiHandler";
import { selectedActionAtom, selectedServiceAtom } from "../../state/atoms"
import { useRecoilValue } from "recoil";

const AddKeywordForm = ({ onUpdate, dismissPanel }) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const { handleSubmit, control, formState: { errors } } = useForm();
    //global state
    const selectedAction = useRecoilValue(selectedActionAtom);
    // const selectedService = useRecoilValue(selectedServiceAtom);

    const onSubmit = data => {
        let d = { ...data, actionId: selectedAction.id };
        setSubmittingTrue();
        createKeyword(d)
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
                    <Controller name="firstKey" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="First key"
                            errorMessage={errors.name && "First key of the SMS is required"}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="regex" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="RegUlar Expression"
                            errorMessage={errors.name && "SMS RegEx is required"}
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

export default AddKeywordForm;
