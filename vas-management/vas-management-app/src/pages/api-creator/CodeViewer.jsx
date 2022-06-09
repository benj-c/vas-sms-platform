//lib
import { useEffect, useState } from "react";
import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-xml"
import "ace-builds/src-noconflict/theme-one_dark"
import "ace-builds/src-noconflict/ext-language_tools";
import { useRecoilValue } from 'recoil'
//app
import { formatXml } from "../../common/AppUtils";
import { processedXmlAtom } from '../../state/atoms'

const CodeViewer = () => {
    const [xml, setXml] = useState();
    const processedXml = useRecoilValue(processedXmlAtom);

    useEffect(() => {
        if (processedXml != null) {
            setXml(formatXml(processedXml))
        }
    }, [processedXml])

    return (
        <AceEditor
            style={{ height: "90vh", width: "100%" }}
            placeholder="API XML"
            mode="xml"
            theme="one_dark"
            name="basic-code-editor"
            onChange={(e) => console.log(e)}
            fontSize={18}
            showPrintMargin={true}
            showGutter={true}
            highlightActiveLine={true}
            value={xml}
            setOptions={{
                enableBasicAutocompletion: true,
                enableLiveAutocompletion: true,
                enableSnippets: true,
                showLineNumbers: true,
                tabSize: 4,
            }} />
    )
}

export default CodeViewer;
