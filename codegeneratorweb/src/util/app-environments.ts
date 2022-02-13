
export interface AppEnvironment {
  value: number;
  environment: string;
}

const appEnvironments: AppEnvironment[] = [
  { value: 0, environment: 'Springboot' },
  { value: 2, environment: 'Django' }
]

export default appEnvironments;
