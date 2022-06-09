//lib
import { useState } from "react";
import { ActionButton, DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField, Toggle } from "@fluentui/react";
import { Controller, useForm } from "react-hook-form";
import { useBoolean } from "@fluentui/react-hooks";
//app
import { updateKeyword } from "../ApiHandler";

const UpdateKeywordForm = ({ id, regex, firstKey, onUpdate, dismissPanel }) => {
    const [isSubmitting, { setTrue: setSubmittingTrue, setFalse: setSubmittingFalse }] = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const { handleSubmit, control, formState: { errors } } = useForm({
        defaultValues: {
            regex: regex,
            firstKey: firstKey,
        }
    });

    const onSubmit = data => {
        let d = { ...data, id: id };
        setSubmittingTrue();
        updateKeyword(d)
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
                        Update
                    </PrimaryButton>
                    <DefaultButton onClick={dismissPanel} style={{ marginLeft: '1rem' }} iconProps={{ iconName: 'CalculatorMultiply' }}>
                        Close
                    </DefaultButton>
                </div>
            </form>

            <form>
                <div style={{border: '1px solid #FFA500', position: 'absolute', bottom: '1rem', right: '1rem', left: '1rem', padding: '0.5rem'}}>
                    <h2>Delete this keyword</h2>
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

export default UpdateKeywordForm;
