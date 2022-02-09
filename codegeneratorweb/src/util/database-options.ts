
export interface DatabaseOption {
  database: string;
  value: number;
}

export const inmemoryLists: DatabaseOption = {
  database: 'In memory lists',
  value: 0
}

export const sqlStore: DatabaseOption = {
  database: 'SQL data store',
  value: 1
}

const databaseOptions: DatabaseOption[] = [
  inmemoryLists,
  sqlStore
]

export default databaseOptions;
