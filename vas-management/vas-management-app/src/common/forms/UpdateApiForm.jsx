
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField } from "@fluentui/react";
import { useState } from "react";
import { Controller, useForm } from "react-hook-form";
import { useRecoilState } from "recoil";

import useBoolean from "../../common/hooks/useBoolean";
import { updateApi } from "../../common/ApiHandler"
import { apiUpdateEventAtom } from "../../state/atoms"

const UpdateApiForm = ({ dismissPanel, api }) => {
    const { value: isSubmitting, toggle: toggleSubmit } = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const [apiUpdateEvent, setapiUpdateEventAtom] = useRecoilState(apiUpdateEventAtom);

    const { handleSubmit, control, formState: { errors } } = useForm({
        defaultValues: {
            name: api.name,
            description: api.description,
        }
    });

    const onSubmit = (data) => {
        let d = { ...api, ...data };
        delete d.graphElements;
        updateApi(d)
            .then(res => {
                setapiUpdateEventAtom(d);
                dismissPanel();
            })
            .catch(e => { })
            .finally(() => { })
    }

    return (
        <div style={{ color: '#e7e7e7' }}>
            <form>
                <div className="form-field">
                    <Controller name="name" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="API name"
                            errorMessage={errors.name && "API name is required"}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="description" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="API description"
                            errorMessage={errors.description && "API description is required"}
                            multiline
                            rows={3}
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

export default UpdateApiForm;
