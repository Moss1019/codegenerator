
export interface CodegenerateRequest {
  projectName: string;
  rootName: string;
  definition: string;
  databaseType: number;
  environment: number;
  externalSystems: number[];
  frontendSystems: number[];
};

export const empty: CodegenerateRequest = {
  databaseType: 0,
  definition: '',
  environment: -1,
  externalSystems: [],
  frontendSystems: [],
  projectName: '',
  rootName: ''
};

export const example: CodegenerateRequest = {
  databaseType: 0,
  definition: 
`agent {  
  agent_id int primary auto_increment
  username string unique
}

item {  
  item_id guid primary
  title string unique
  owner_id int foreign(agent)
}

collaborator {  
  agent_id int foreign(agent) primary
  collaborator_id int foreign(agent) secondary
  looped(collaborator)
}

item_collaborator {  
  item_id guid foreign(item) primary
  collaborator_id int foreign(agent) secondary
  joined(item_collaborator)
}`,
  environment: 0,
  externalSystems: [0, 1],
  frontendSystems: [0],
  projectName: 'example',
  rootName: 'app'
}
