import { TextareaAutosize, Typography, styled } from "@mui/material";
import { useRef } from "react";
import StyledPaper from "../controls/styled-paper";

export interface DefinitionProps { 
  definition: string;
  onChange: (definition: string) => void
}

const StyledTextarea = styled(TextareaAutosize)(({ theme }) => ({
  width: 'calc(100% - .5rem)',
  backgroundColor: theme.palette.mode === 'light' ? '#f3f3f3' : '#252525',
  color: theme.palette.mode === 'light' ? '#010101' : '#f7f7f7'
}));

function Definition({
  definition,
  onChange
}: DefinitionProps) {
  const textarea = useRef<HTMLTextAreaElement>(null);

  const currentInBraces: () => boolean = () => {
    const currentPos = textarea.current!.selectionEnd;
    const text = textarea.current!.textContent || "";
    let openBrace = false;
    let closeBrace = false;
    for (let i = currentPos; i >= 0; --i) {
      if (text[i] === "{") {
        openBrace = true;
        break;
      }
      if (text[i] === "}") {
        break;
      }
    }
    for (let i = currentPos; i < text.length; ++i) {
      if (text[i] === "}") {
        closeBrace = true;
        break;
      }
      if (text[i] === "{") {
        break;
      }
    }
    return openBrace && closeBrace;
  };

  const onKeyDown = (ev: any) => {
    if (ev.key === "Tab" && !ev.shiftKey) {
      ev.preventDefault();
      document.execCommand("insertText", false, "  ");
      return false;
    } else if (ev.key === "{") {
      ev.preventDefault();
      document.execCommand("insertText", false, "{\n}");
      textarea.current!.selectionEnd -= 2;
      document.execCommand("insertText", false, "  ");
    } else if (ev.keyCode === 13) {
      if (currentInBraces()) {
        ev.preventDefault();
        document.execCommand("insertText", false, "\n  ");
      }
    }
  }

  return (
    <StyledPaper>
      <Typography variant="h5" color="primary">
        Definition
      </Typography>
      <StyledTextarea 
        ref={textarea}
        minRows={15}
        value={definition}
        onKeyDown={onKeyDown}
        onChange={(ev: any) => onChange(ev.target.value)}
      />
    </StyledPaper>
  )
}

export default Definition;
