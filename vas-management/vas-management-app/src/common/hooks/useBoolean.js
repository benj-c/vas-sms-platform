import { useState } from "react";

const useBoolean = defaultValue => {
    const [value, setValue] = useState(!!defaultValue)

    const setTrue = () => setValue(true)
    const setFalse = () => setValue(true)
    const toggle = () => setValue(x => !x)

    return { value, setValue, setTrue, setFalse, toggle }
}

export default useBoolean;
