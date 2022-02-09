import { 
  styled, 
  Container, 
  Paper, 
  Typography, 
  Box
} from "@mui/material";
import ContentBox from "../controls/content-paper";

const MarginedPaper = styled(Paper)(() => ({
  marginTop: '1rem',
  padding: '.75rem'
}))

const CodeBox = styled(Box)(() => ({
  marginTop: '.55rem',
  marginBottom: '.55rem'
}))

function SyntaxGuide() {
  return (
    <ContentBox>
      <Container>
        <MarginedPaper>
          <Typography>
            This app lets you create a whole lot of boiler plate code to get
            started on creating a web application in Java, C# and TypeScript.
            Enter the definitions for the business objects, select the
            environment details and hit generate, this will create a few files
            to jump start the development of a backend server.
          </Typography>
        </MarginedPaper>

        <MarginedPaper>
          <Typography>The syntax is fairly straight forward. It allows you to:</Typography>
          <ul>
            <li>
              Specify business objects
            </li>
            <li>
              Specify their fields, including the datatype
            </li>
            <li>
              Set up relationships between objects. One-to-many and many-to-many
            </li> 
          </ul>
        </MarginedPaper>

        <MarginedPaper>
          <Typography variant="h6">
            Create an object definition with:
          </Typography>
          <CodeBox>
            <code>
              object_name &#123;
              <br />
              &#125;
            </code>
          </CodeBox>
          <Typography variant="h6">Data types</Typography>
          <Typography>
            Define the fields of the object by specifying the field name, data
            type and 0 or more options.
          </Typography>
          <ul>
            <li>int</li>
            <li>boolean</li>
            <li>string</li>
            <li>char</li>
            <li>date</li>
            <li>guid</li>
          </ul>
          <Typography variant="h6">Field options</Typography>
          <ul>
            <li>primary</li>
            <li>auto_increment</li>
            <li>secondary</li>
            <li>unique</li>
            <li>foreign</li>
          </ul>
          <Typography variant="h6">Putting these together gives:</Typography>
          <CodeBox>
            <code>
              object_name &#123;
              <br />
              &emsp;object_id guid primary <br />
              &emsp;object_title string unique <br />
              &emsp;created date
              <br />
              &emsp;db_id int auto_increment <br />
              &#125;
            </code>
          </CodeBox>
        </MarginedPaper>
        <MarginedPaper>
          <Typography>
            Create a one-to-many relationship by specifying a field in the child
            object with the same data type as the primary field in the parent
            object, and mark it with foreign. 'foreign' takes the parent
            object's name as an argument.
          </Typography>
          <CodeBox>
            <code>
              parent_obj &#123;
              <br />
              &emsp;parent_id guid primary <br />
              &#125;
            </code>
          </CodeBox>
          <CodeBox>
            <code>
              child_obj &#123;
              <br />
              &emsp;child_id guid primary <br />
              &emsp;parent_id guid foreign(parent_obj) <br />
              &#125;
            </code>
          </CodeBox>
          <Typography className="mt-4">
            Create a many-to-many relationship by specifying a field in the
            child object with the same data type as the primary field in the
            parent object, mark it with foreign and indicate if the object is
            connected with another object or connected with instances of itself
          </Typography>
          <CodeBox>
            <code>
              parent_obj &#123;
              <br />
              &emsp;parent_id guid primary <br />
              &#125;
            </code>
          </CodeBox>
          <CodeBox>
            <code>
              child_obj &#123;
              <br />
              &emsp;child_id guid primary <br />
              &#125;
            </code>
          </CodeBox>
          <CodeBox>
            <code>
              joining_obj &#123;
              <br />
              &emsp;parent_id guid foreign(parent_obj) primary <br />
              &emsp;child_id guid foreign(child_obj) secondary <br />
              &emsp;joined(joining_field_name)
              <br />
              &#125;
            </code>
          </CodeBox>
        </MarginedPaper>
      </Container>
    </ContentBox>
  );
}

export default SyntaxGuide;