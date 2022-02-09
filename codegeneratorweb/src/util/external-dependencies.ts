
export interface ExternalDependency {
  value: number;
  dependency: string;
}

export interface FrontendDependency {
  value: number;
  dependency: string;
}

export const kafkaDependency: ExternalDependency = {
  dependency: 'Kafka messaging',
  value: 0
}

export const elasticSearchDependency: ExternalDependency = {
  dependency: 'Elastic Search',
  value: 1
}

export const reactDependency: FrontendDependency = {
  dependency: 'Axios with Typescript',
  value: 0
}

export const flutterDependency: FrontendDependency = {
  dependency: 'Flutter with Dart',
  value: 1
}

const externalDependencies: ExternalDependency[] = [
  elasticSearchDependency,
  kafkaDependency
]

export default externalDependencies;
