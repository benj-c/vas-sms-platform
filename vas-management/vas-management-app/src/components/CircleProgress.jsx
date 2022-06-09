import React, { useEffect, useState } from "react";

const sqSize = 100, strokeWidth = 5;
const CircleProgress = ({ value, name }) => {
    const doSum = () => {
        const radius = 40;
        const viewBox = `0 0 ${sqSize} ${sqSize}`;
        const dashArray = (radius * Math.PI * 2).toFixed(2);
        const dashOffset = (dashArray - dashArray * value / 100).toFixed(2);
        const percentage = value;
        let d = {
            radius,
            viewBox,
            dashArray,
            dashOffset,
            percentage
        };
        return d;
    }
    const [data, setData] = useState(doSum());

    useEffect(() => {
        setData(doSum());
    }, [value])

    return (
        <div className="pt-4">
            <svg width={sqSize} height={sqSize} viewBox={data.viewBox}>
                <circle
                    fill="none"
                    stroke="#ddd"
                    cx={sqSize / 2}
                    cy={sqSize / 2}
                    r={data.radius}
                    strokeWidth={`${strokeWidth}px`} />
                <circle
                    stroke="var(--progressColor)"
                    fill="none"
                    stokeLineCap="round"
                    strokeLineJoin="round"
                    cx={sqSize / 2}
                    cy={sqSize / 2}
                    r={data.radius}
                    strokeWidth={`${strokeWidth}px`}
                    transform={`rotate(-90 ${sqSize / 2} ${sqSize / 2})`}
                    style={{
                        strokeDasharray: data.dashArray,
                        strokeDashoffset: data.dashOffset
                    }} />
                <text
                    className="text-base font-semibold"
                    fill="var(--black)"
                    x="50%"
                    y="50%"
                    dy=".3em"
                    textAnchor="middle">
                    {`${data.percentage}%`}
                </text>
            </svg>
            <p style={{ textTransform: "capitalize" }}>{name}</p>
        </div>
    )
}

export default CircleProgress;
