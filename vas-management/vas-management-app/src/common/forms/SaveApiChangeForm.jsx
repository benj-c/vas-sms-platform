
import { DefaultButton, MessageBar, MessageBarType, PrimaryButton, TextField } from "@fluentui/react";
import { useState } from "react";
import { Controller, useForm } from "react-hook-form";
import { useRecoilState } from "recoil";
import { useHistory } from "react-router";

import useBoolean from "../hooks/useBoolean";
import { commitApiXml } from "../ApiHandler"
import { apiUpdateEventAtom } from "../../state/atoms"

const SaveApiChangeForm = ({ dismissPanel, api }) => {
    const history = useHistory();
    const { value: isSubmitting, toggle: toggleSubmit } = useBoolean(false);
    const [submitDetail, setSubmitDetail] = useState(null);
    const [apiUpdateEvent, setapiUpdateEventAtom] = useRecoilState(apiUpdateEventAtom);

    const { handleSubmit, control, formState: { errors } } = useForm();

    const onSubmit = (data) => {
        let d = { ...api, ...data };
        console.log(api, data)
        let ad = {
            apiId: api.id,
            version: data.version,
            xml: d.xml,
            commitMessage: data.commitMessage
        }
        commitApiXml(ad)
            .then(res => {
                // setapiUpdateEventAtom({id: ad.apiId, commitId: res.data});
                dismissPanel();
                history.push(`/api-creator?ref=${ad.apiId}&commit=${res.data}`)
            })
            .catch(e => { })
            .finally(() => { })
    }

    return (
        <div style={{ color: '#e7e7e7' }}>
            <form>
                <div className="form-field">
                    <Controller name="commitMessage" control={control} rules={{ required: true }} render={({ field }) =>
                        <TextField
                            {...field}
                            label="Commit message"
                            errorMessage={errors.commitMessage && "Commit message is required"}
                            multiline
                            rows={4}
                        />
                    }
                    />
                </div>
                <div className="form-field">
                    <Controller name="version" control={control} render={({ field }) =>
                        <TextField
                            {...field}
                            label="API version"
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

export default SaveApiChangeForm;
