//lib
import { useEffect, useState } from "react";
import { useRecoilValue } from 'recoil'
import Highlight from 'react-highlight'
//app
import { formatXml } from "../../common/AppUtils";
import { processedXmlAtom } from '../../state/atoms'

const CodeViewer = () => {
    const [xml, setXml] = useState();
    const processedXml = useRecoilValue(processedXmlAtom);

    useEffect(() => {
        if (processedXml != null) {
            setXml(formatXml(processedXml, "    "))
        }
    }, [processedXml])

    return (
        <Highlight className='xml'>
            {xml}
        </Highlight>
    )
}

export default CodeViewer;
