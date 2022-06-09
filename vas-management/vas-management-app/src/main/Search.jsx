//lib
import { useCallback, useRef } from "react";
import { createUseStyles } from 'react-jss';
//app

const useStyles = createUseStyles({
    search: {
        marginLeft: 'auto',
        display: 'flex',
        alignItems: 'center',
        position: 'relative',
        '& input': {
            borderRadius: '0.2rem',
            fontSize: '1rem',
            outline: 'none',
            border: 'none',
            padding: '0.45rem 1rem 0.45rem 2rem',
            background: 'rgba(255, 255, 255, 0.04)',
            color: '#fff',
            '&::placeholder': {
                color: 'rgba(255, 255, 255, 0.3)',
                opacity: 1
            }
        },
        '& i': {
            fontSize: '0.8rem',
            position: 'absolute',
            marginLeft: '0.4rem'
        }
    },
})

const Search = () => {
    const classes = useStyles();
    const searchInput = useRef();

    const onSearch = useCallback((e) => {
        if (e.keyCode === 13) {
            let search = searchInput.current.value;
            console.log(search);
        }
    }, [])

    return (
        <div className={classes.search}>
            <i className="ms-Icon ms-Icon--Search" aria-hidden="true"></i>
            <input
                type="text"
                placeholder="Search services, APIs"
                onKeyUp={onSearch}
                ref={searchInput}
            />
        </div>
    )
}

export default Search;
