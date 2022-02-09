
export interface RouteInfo {
  id: number;
  routeText: string;
  routePath: string;
}

const routes: RouteInfo[] = [
  {
    id: 0,
    routePath: '/',
    routeText: 'Code generator'
  },
  {
    id: 1,
    routePath: '/guide',
    routeText: 'Syntax guide'
  },
  {
    id: 2,
    routePath: '/guids',
    routeText: 'Guid generator'
  }
]

export default routes;
